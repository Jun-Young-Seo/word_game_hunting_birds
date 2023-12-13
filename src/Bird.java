import javax.swing.*;

    //단어를 가진 새
    //모든 새들의 부모 클래스가 되는 추상클래스
public abstract class Bird {
    //현재 진행중인 라운드. GameGround에서 설정돼서 새의 속도를 결정하는데 사용
    protected int round;
    protected JTextField userInput;//게임그라운드 입력창 레퍼런스 연결
        //textSource에서 받아와서 단어 설정
    protected String word;
    //살아있는지에 대한 Flag. NoHuntBird와 단어 사냥에서 사용
    private boolean isAlive = true;
    //GameGround 메소드와 remove, add를 위한 레퍼런스
    protected GameGround gameGround;
    //단어 소스를 위해 사용하는 레퍼런스
    protected TextSource textSource = new TextSource();
    //폭탄 사용을 위한 플래그
    protected boolean isMoving = true;

    public Bird(JTextField userInput, int round,GameGround gameGround) {
        //userInput은 새에 적혀있는 문자열과 입력을 확인하기 위한 레퍼런스
        //round는 새의 속도 조절을 위해 GameGround가 전달
        //gameGround는 remove와 add를 위해 레퍼런스 전달
        this.gameGround=gameGround;
        this.round = round;
        this.userInput = userInput;
        //textSource의 getNext()호출로 다음 단어로 지정
        this.word = setWord();
    }

    //폭탄이 터지면 실행. 새 움직임 일시정지
    public void pauseMovement() {
        isMoving = false;
    }

    // 새 움직임을 재개하는 메소드
    public void resumeMovement() {
        isMoving = true;
    }
    //textSource에서 txt 파일을 읽어 다음 단어를 읽어오는 메소드
    public String setWord() {
        return textSource.getNext();
    }
    //현재 word 반환 메소드
    public String getWord() {
        return word;
    }
    //살아있는지 반환. noHuntThread와 단어 입력 창에서 사용
    public boolean getIsAlive() {
        return isAlive;
    }
    //noHuntThread에서 사냥하면 안되는 새를 잡았다고 판단할 때 사용
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    //추상 메서드
    //상속받은 하위 새 클래스들에서 사용
    public abstract boolean isCorrect();
    public abstract void moveWings();
    public abstract JLabel getBirdLabel();
    public abstract ImageIcon getBirdImage();
    public abstract void setCorrectFlag(boolean correctFlag);
    public abstract Thread getThread();
}

