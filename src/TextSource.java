import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

//word source 파일에서 단어를 읽어오는 클래스
public class TextSource {
    //원본단어 5965개
    //로딩 속도를 위해 벡터의 초기 크기 6000개로 설정
    //벡터 내 랜덤 단어를 쉽개 얻어내고, 추가하기 위해 배열 아닌 벡터 사용
    private Vector<String> wordVector = new Vector<String>(6000);
    public TextSource(){
        //int count =0;
        try {
            //source 폴더/ korean.txt를 FileRead
            Scanner scanner = new Scanner(new FileReader("source/korean.txt"));
            while(scanner.hasNext()){
                String word = scanner.nextLine();
                wordVector.add(word);
                //count++;
            }
            //System.out.println(count);
            
        }
        catch (FileNotFoundException e){//예외처리
            System.out.println("no file exist");
            System.exit(0);
        }
    }
    //벡터의 다음 단어 리턴
    //새와 폭탄 문자열 표시에 사용
    public String getNext(){
        //벡터의 크기 변수에 저장
        int wordVectorsize = wordVector.size();
        //벡터 크기만큼 random Int 생성
        int idx = (int)(Math.random()*wordVectorsize);
        //그 인덱스에 해당하는 단어 리턴
        return wordVector.get(idx);
    }
}
