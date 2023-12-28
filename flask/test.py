import os;

#이미지와 AI 모델이 저장된 경로
forAiPath = os.path.join("home", "ubuntu", "ai")

#AI가 접근할 저장된 이미지의 경로
inputImgDir = os.path.join(forAiPath, "inputImg")

#이미지가 저장될 경로
targetPath = os.path.join(inputImgDir, "Target")


result1 = os.listdir(forAiPath)
result2 = os.listdir(inputImgDir)
result3 = os.listdir(targetPath)

print(result1)
print(result2)
print(result3)
