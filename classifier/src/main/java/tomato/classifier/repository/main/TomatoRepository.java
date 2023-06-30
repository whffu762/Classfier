package tomato.classifier.repository.main;

import org.springframework.stereotype.Repository;
import tomato.classifier.data.ResultData;

@Repository
public class TomatoRepository {
//?? 이거 목저이 뭐임 .... repository 역할도 안하는거 같은데?
    private static ResultData finalData = new ResultData(); //이거 왜 static으로 함?

    public ResultData save(ResultData data){
        finalData = data;
        return finalData;
    }

    public ResultData find(){
        return finalData;
    }



}
