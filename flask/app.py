from flask import Flask
import os

#db에 질병 설명 저장할 때 학습시킨 순서대로 번호 매겨서 저장해야함 글자가 지멋대로 돼 있는듯
#학습시킨 순서대로 정렬이 돼 있어야 함

#플라스크 객체 생성
#__name__에 객체 변수명(app)이 저장됨
app = Flask(__name__)

#이미지와 AI 모델이 저장된 경로
forAiPath = os.path.join("home", "ubuntu", "ai")

#AI가 접근할 저장된 이미지의 경로
inputImgDir = os.path.join(forAiPath, "inputImg")

#이미지가 저장될 경로
targetPath = os.path.join(inputImgDir, "Target")

#이미지 저장될 개수
input_num = 5


import pymysql
from setting import host;
from setting import dbName;
from setting import user;
from setting import password;

#DB 연결
def Dbconnect():
    conn = pymysql.connect(host = host,
                      user = user,
                      password = password,
                      db = dbName,
                      charset = "utf8")

    return conn

#DB 연결 끊기
def Dbclose(conn):
    conn.close

#DB에서 label(질병명) 추출출
def Select_id(TABLE, DISEASE_ID):
    
    conn = Dbconnect()
    cursor = conn.cursor()

    sql = '''SELECT {id_code} 
    FROM {table}
    ORDER BY {id_code}'''.format(table = TABLE, id_code = DISEASE_ID)

    cursor.execute(sql)
    result = cursor.fetchall()
    
    result_list = [0] * len(result)


    for i in range(len(result)):
        result_list[i] = result[i][0]
    
    Dbclose(conn)

    return result_list



#이미지 저장
@app.route("/predict", methods=['GET'])   #왜 get으로도 받는지는 기능 다 완성되면 실험
def predict():
    response = mainPredict()
    clearFolder(targetPath) #요청 처리가 끝나면 폴더 내 이미지 제거
    
    return  response

#폴더 내 파일 삭제 - 한 번 predict한 후 input 이미지가 지워줘야 함
def clearFolder(Path) :
    if os.path.exists(Path):
        for file in os.scandir(Path):
            os.remove(file.path)


import torch
import torch.nn as nn
from torch.utils.data import DataLoader
from torchvision import models, transforms
from tqdm import tqdm
from collections import Counter
from torch.utils.data import Dataset
import json
from PIL import Image

#데이터 전처리
#@app.route("/predict", methods=['POST'])  
def mainPredict():
    #입력 데이터가 저장될 경로
    data_path = inputImgDir
    #batch_size 
    b_size = 1

    trans_test = transforms.Compose([transforms.Resize((224, 224)),
                                           transforms.ToTensor(),
                                           transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])

    test_dataset = CustomImageDataset(data_set_path=data_path, transforms=trans_test)

    test_loader = DataLoader(test_dataset, num_workers=2, batch_size=b_size, shuffle=True)

    json_object = predictDensenet(test_loader, b_size)
    
    return json_object 


#예측을 위한 모델 준비
def predictDensenet(test_loader, b_size):

    #모델 틀 만들기
    model = models.densenet161(weights='DenseNet161_Weights.DEFAULT')
    num_classes = 9
    num_ftrs = model.classifier.in_features
    model.classifier = nn.Linear(num_ftrs, num_classes)

    #모델이 저장된 경로
    model_path = os.path.join(forAiPath, "best_densenet-30.pth")

    #사용할 장치(GPU or CPU)
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
    
    #틀에 학습된 파라미터 적용(gpu에서 학습된 모델을 cpu에 로드드)
    model.load_state_dict(torch.load(model_path, map_location=device))

    #gpu에서 학습된 모델을 gpu에 로드할 때
    #model.load_state_dict(torch.load(model_path))
    
    #장치에 모델 적용용
    net = model.to(device)

    return predict(test_loader, device, net, b_size)
 
#예측 결과로 JSON 데이터 만들기
def predict(test_loader, device, net, b_size):

    #예측값이 저장될 리스트트
    result = [0] * input_num

    #예측 시작
    result = doPredict(test_loader, device, net, b_size, result)

    value = result.values()
    total = sum(value)

    max_key = max(result, key=result.get)
    max_value = result.get(max_key) / total * 100

    
    json_dict = {}
    json_dict['name'] = max_key
    json_dict['prob'] = max_value
    json_object = json.dumps(json_dict)

    return json_object
 
#예측
def doPredict(val_loader, device, net, b_size, result):
    net.eval()  #예측 모드로 변경
    with torch.no_grad():
        loop = tqdm(enumerate(val_loader), total=len(val_loader))

        for i, data in loop:
            inputs = data['image'].to(device)

            #질병 후보들을 DB에서 받아오는 방식으로 함
            #첫번째 파라미터 : 테이블명
            #두번째 파라미터 : 테이블에서 가져올 id
            label_list = Select_id("disease", "id")

            #model에 데이터 입력
            outputs = net(inputs)

            #예측
            _, predicted = torch.max(outputs, 1)

            #예측값 저장
            for j in range(b_size):
                result[i * b_size + j] = label_list[predicted[j]]

        result = Counter(result)

    return result
            
    
class CustomImageDataset(Dataset):

    def get_class_names(self):
        return os.walk(self.data_set_path).__next__()[1]

    def read_data_set(self):

        all_img_files = []
        all_labels = []

        class_names = os.walk(self.data_set_path).__next__()[1]

        temp_class_name = class_names.copy()


        for index, class_name in enumerate(temp_class_name):
            label = index
            img_dir = os.path.join(self.data_set_path, class_name)
            img_files = os.walk(img_dir).__next__()[2]

            for img_file in img_files:
                img_file = os.path.join(img_dir, img_file)
                img = Image.open(img_file)
                if img is not None:
                    all_img_files.append(img_file)
                    all_labels.append(label)

        return all_img_files, all_labels, len(all_img_files), len(class_names), temp_class_name

    def __init__(self, data_set_path, transforms):
        self.data_set_path = data_set_path
        self.image_files_path, self.labels, self.length, self.num_classes, self.class_names = self.read_data_set()
        self.transforms = transforms


    def __getitem__(self, index):
        image = Image.open(self.image_files_path[index])

        image = image.convert("RGB")
        path = self.image_files_path[index]

        if self.transforms is not None:
            image = self.transforms(image)

        return {'image': image, 'label': self.labels[index] }

    def __len__(self):
        return self.length



if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0')

