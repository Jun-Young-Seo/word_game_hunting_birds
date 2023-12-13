import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameGround extends JPanel {
    //폭탄 클래스 Bomb 레퍼런스
    Bomb bomb;
    //Bird와 Bomb에 gameGround 레퍼런스 전달하기 위한 변수
    GameGround gameGround = this;
    //잡으면 안되는 새를 출력하기 위한 변수
    protected int noHuntBirdNum;
    //scorePanel에 전달할 round 변수
    protected int round;
    //오답 입력 처리를 위한 플래그
    private boolean isNotCorrected = true;
    //4종류의 Bird 배열
    protected Bird[] birds;
    //틀렸을경우 나오는 이미지
    private ImageIcon no = new ImageIcon("images/no.png");
    //배경화면
    private ImageIcon sky = new ImageIcon("images/sky.jpg");
    //배걍화면
    private Image skyImg = sky.getImage();
    //게임 패배시(하트 다 쓰면) 출력되는 이미지
    private ImageIcon loseImg = new ImageIcon("images/lose.png");
    ScorePanel scorePanel;

    //틀렸을 경우를 표기하는 레이블
    JLabel notAnswer;
    //잡으면 안되는 새를 표시할 스레드
    NohuntThread noHuntThread;
    //잡으면 안되는 새를 나타내는 이미지 배열
    protected ImageIcon[] noHuntBirdImg;
    //게임의 메인 로직 스레드
    GameThread mainGameThread;
    //사용자 입력창
    private JTextField typeInput = new JTextField(30);
    //메인 메뉴로 돌아가기 위한 GamePanel 레퍼런스
    private GamePanel gamePanel;


    @Override
    protected void paintComponent(Graphics g) {
        //배경화면 그리기용 오버라이딩
        super.paintComponent(g);
        g.drawImage(skyImg, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    //메인메뉴로 돌아가기 위해 gamePanel 레퍼런스를 전달
    //점수와 랭킹 표기를 위해 scorePanel 레퍼런스 전달
    public GameGround(ScorePanel scorePanel, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        //시작시 라운드는 1
        round = 1;

        birds = new Bird[4];
        //각 새에 레퍼런스를 전달하며 생성
        birds[0] = new Sparrow(typeInput, round, gameGround);
        birds[1] = new Eagle(typeInput, round,gameGround);
        birds[2] = new MiniBird(typeInput,round,gameGround);
        birds[3] = new Parrot(typeInput,round,gameGround);
        birds[0].getBirdLabel().setSize(300, 300);
        birds[0].getBirdLabel().setLocation(200, 100);
        birds[1].getBirdLabel().setSize(300, 300);
        birds[1].getBirdLabel().setLocation(1000, 100);
        birds[2].getBirdLabel().setSize(300,300);
        birds[2].getBirdLabel().setLocation(200,400);
        birds[3].getBirdLabel().setSize(300,300);
        birds[3].getBirdLabel().setLocation(1000,400);
        add(birds[0].getBirdLabel());
        add(birds[1].getBirdLabel());
        add(birds[2].getBirdLabel());
        add(birds[3].getBirdLabel());
        //초기(1라운드) 새 생성 끝

        //잡으면 안되는 새의 이미지를 담는 변수
        noHuntBirdImg = new ImageIcon[birds.length];
        for (int i = 0; i < birds.length; i++) {
            noHuntBirdImg[i] = birds[i].getBirdImage();
        }

        //오답을 입력했을 경우를 위한 레이블
        //평상시에는 더해져 있다가 보이지 않음
        //오답시 setIcon을 통해 잠시 X 표시를 보여줌
        notAnswer = new JLabel();
        notAnswer.setSize(500, 500);
        notAnswer.setLocation(500, 300);
        add(notAnswer);

        //scorePanel 레퍼런스 설정
        this.scorePanel = scorePanel;
        scorePanel.setRound(round);

        //사용자 입력창
        typeInput.setSize(1000, 50);
        typeInput.setFont(new Font("고딕체", Font.BOLD, 30));
        typeInput.setLocation(300, 800);
        add(typeInput);

        setLayout(null);

        //게임 실행 메인 스레드 시작
        mainGameThread = new GameThread();
        mainGameThread.start();

        //사용자 입력창 액션 리스너 설정
        typeInput.addActionListener(new TextActionListener());
        }

        //사용자 입력창 액션 리스너
        class TextActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //잡으면 안되는 새를 잡았을 경우를 검출
                noHuntBirdNum = mainGameThread.getRandNum();
                if (birds[noHuntBirdNum].isCorrect()) {
                    scorePanel.decrease();
                }

                //진짜 정답을 맞춘 경우
                else if (birds[0].isCorrect() || birds[1].isCorrect() || birds[2].isCorrect() || birds[3].isCorrect()) {
                    scorePanel.increase();
                }

                //폭탄에 입력된 정답을 맞췄을 경우
                //폭탄은 점수에는 관여하지 않음
                //이 문장은 아무것도 실행하지 않지만,
                //else의 오답인 경우에서 처리되지 않기 위해 이 문장이 필요함
                else if (bomb!=null && bomb.isCorrect()){
                }

                //오답인 경우
                else {
                    scorePanel.decrease();
                    //오답 플래그 down
                    isNotCorrected = false;
                }
                //사용자가 3번 틀려서 게임이 끝나는 경우
                if(scorePanel.isLose()){
                    //랭킹 저장을 위해 round 전달
                    scorePanel.setRound(round);
                    //메인 게임 스레드의 패배함수 실행
                    mainGameThread.lose();
                }
                //입력창 초기화
                typeInput.setText("");
            }
        }

        //폭탄의 답을 입력한 경우 모든 새 3초간 정지
    public void pauseAllBirds() {
        for (Bird bird : birds) {
            bird.pauseMovement();
        }
    }
    //3초 뒤 새를 움직이게 하는 함수
    public void resumeAllBirds() {
        for (Bird bird : birds) {
            bird.resumeMovement();
        }
    }

        //잡으면 안되는 새를 표시하는 스레드
        class NohuntThread extends Thread {
            //잡으면 안되는 새의 인덱스
            protected int randNum;
            //"사냥하면 안돼!" 표시용 레이블
            JLabel noHunt;

            //스레드 시작시 숫자를 전달받아 사냥하면 안되는 새로 설정
            public NohuntThread(int randNum) {
                this.randNum = randNum;
            }

            //사냥하면 안되는 새 인덱스 설정용 함수
            public void setRandNum(int randNum) {
                this.randNum = randNum;
            }

            //메인 게임 스레드와 동기화
            //모든 새를 잡았을 경우를 위해 동기화함
            @Override
            synchronized public void run() {
                while (true) {
                    //잡으면 안되는 새 이미지
                    ImageIcon noHuntImg = noHuntBirdImg[randNum];
                    noHunt = new JLabel(noHuntImg);
                    noHunt.setText("는 필요없어! 사냥하면 안돼!");
                    noHunt.setFont(new Font("고딕체", Font.BOLD, 30));
                    noHunt.setSize(500, 200);
                    noHunt.setLocation(500, 30);
                    noHunt.setHorizontalTextPosition(JLabel.CENTER);
                    noHunt.setVerticalTextPosition(JLabel.BOTTOM);
                    add(noHunt);
                    try {
                        //모든 새 사냥 전까지 스레드 대기
                        wait();
                    } catch (InterruptedException e) {
                        //인터럽트시 제거
                        noHunt.getParent().remove(noHunt);
                        return;
                    }
                    //notify()시 제거
                    noHunt.getParent().remove(noHunt);
                }
            }
        }

        //게임 메인 로직 담당 스레드
        class GameThread extends Thread {
            //패배 표시용 레이블
            private JLabel lose;
            //랭킹 표시를 위한 이름 변수
            private String name;
            //패배 판단용 플래그
            private boolean loseFlag=false;
            //랜덤으로 나오는 잡으면 안되는 새를 설정하는 변수
            private int randNum = (int) (Math.random() * birds.length);
            public int getRandNum() {
                return randNum;
            }
            //폭탄이 나올 확률과 위치를 바꿔주게 될 변수
            private int bombRandNum = (int)(Math.random()*100+1);
            //패배시 호출되는 함수
            public void lose(){
                //lose 레이블을 만들고 이미지를 붙여 나타낸다
                lose = new JLabel();
                lose.setIcon(loseImg);
                lose.setSize(1024,1024);
                lose.setLocation(300,0);

                //모든 새 삭제
                for(int i=0;i<birds.length;i++) {
                    GameGround.this.remove(birds[i].getBirdLabel());
                }
                //폭탄이 있었다면 삭제
                if(bomb!=null) {
                    GameGround.this.remove(bomb.getBombLabel());
                }
                //사용자 입력창 삭제
                typeInput.setSize(0,0);
                //사냥하면 안되는 새 스레드 인터럽트
                //noHuntThread의 레이블은 인터럽트 시 remove됨
                noHuntThread.interrupt();
                GameGround.this.add(lose);
                GameGround.this.repaint();
                //패배 플래그 up
                loseFlag=true;
            }
            @Override
            public void run() {
                //메인 게임 스레드가 실행되면 noHuntThread도 실행
                //1라운드를 위해 랜덤숫자 전달
                noHuntThread = new NohuntThread(randNum);
                noHuntThread.start();
                //잡으면 안되는 새는 사냥하지 않아도 잡은것으로 설정해서 다음 라운드로 넘어가게 함
                birds[randNum].setIsAlive(false);
                while (true) {
                    try {
                        //폭탄 숫자는 1~100까지 나온다
                        //50 미만이면 생성되므로 50%확률로 폭탄 생성
                        //폭탄이 여러개 생기는 경우를 방지하기 위해 bomb==null 조건
                        if(bombRandNum<50 && bomb==null){
                            //폭탄의 문자열 입력 검증을 위해 입력창 레퍼런스 전달
                            //폭탄 삭제를 위해 gameGround 레퍼런스 전달
                            bomb = new Bomb(typeInput,gameGround);
                            bomb.getBombLabel().setSize(300,300);
                            //랜덤한 위치에 생성되는 폭탄
                            bomb.getBombLabel().setLocation(bombRandNum*25,0);
                            add(bomb.getBombLabel());
                        }
                        //패배 플래그 up이면
                        if(loseFlag){
                            //1초간 대기 후 lose 이미지 제거
                            sleep(1000);
                            GameGround.this.remove(lose);
                            //패배시 사용자 이름을 남기기 위한 입력창 생성
                            //이름은 ranking.txt파일에 저장됨
                            JLabel inputName = new JLabel("이름을 입력하세요.");
                            JTextField inputNameField = new JTextField(30);
                            inputName.setSize(1000,30);
                            inputName.setFont(new Font("고딕체",Font.BOLD, 30));
                            inputName.setBackground(Color.RED);
                            inputName.setLocation(630,300);
                            inputNameField.setSize(1000,30);
                            inputNameField.setLocation(300,500);
                            add(inputName);
                            add(inputNameField);
                            //사용자 이름 입력창에 대한 액션 리스너
                            inputNameField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    name = inputNameField.getText();
                                    //ranking.txt에 이름을 남기는 기능은 scorePanel 클래스에 있음
                                    scorePanel.setPlayerName(name);
                                    //이름 입력이 완료되면 초기 메뉴화면으로 돌아감
                                    gamePanel.endGame();
                                }
                            });
                            repaint();
                        }
                        if (!isNotCorrected) {//오답인경우
                            //숨겨져 있던 notAnswer 레이블에 X 이미지 표시
                            notAnswer.setIcon(no);
                            repaint();
                            //2초후 이미지 삭제
                            sleep(2000);
                            notAnswer.setIcon(null);
                            repaint();
                            //다시 오답 플래그 up
                            isNotCorrected = true;
                        }
                        //모든 새가 사냥됐는지 검증하기 위한 플래그
                        boolean isAllBirdDead = true;
                        for (int i = 0; i < birds.length; i++) {
                            if (birds[i].getIsAlive()) {//살아있는 새가 있다
                                isAllBirdDead = false;
                                break;
                            }
                        }
                        sleep(100);
                        if (isAllBirdDead) {//사냥 완료한 시점에
                            //사냥하면 안되는 새를 바꾸기 위한 동기화
                            synchronized (noHuntThread) {
                                //사냥하면 안되는 새의 플래그를 올려 잡은 것으로 인식
                                birds[randNum].setCorrectFlag(true);
                                //다음 라운드로 증가
                                round++;
                                scorePanel.roundNumLabel.setText(Integer.toString(round));
                                for (int i = 0; i < birds.length; i++) {
                                    if (birds[i] != null) {//NullPointer예외 방지
                                        //기존 새 스레드가 정지될 때 까지 대기
                                        birds[i].getThread().join();
                                    }
                                }

                                //라운드가 바뀌면 새로운 새 생성
                                //라운드 변수가 새들에게 전달되면서 속도가 빨라진다
                                birds[0] = new Sparrow(typeInput, round, gameGround);
                                birds[1] = new Eagle(typeInput, round, gameGround);
                                birds[2] = new MiniBird(typeInput, round, gameGround);
                                birds[3]= new Parrot(typeInput,round,gameGround);
                                birds[0].getBirdLabel().setSize(300, 300);
                                birds[0].getBirdLabel().setLocation(200, 200);
                                birds[1].getBirdLabel().setSize(300, 300);
                                birds[1].getBirdLabel().setLocation(1000, 200);
                                birds[2].getBirdLabel().setSize(300, 300);
                                birds[2].getBirdLabel().setLocation(200, 500);
                                birds[3].getBirdLabel().setSize(300,300);
                                birds[3].getBirdLabel().setLocation(1000,400);
                                add(birds[0].getBirdLabel());
                                add(birds[1].getBirdLabel());
                                add(birds[2].getBirdLabel());
                                add(birds[3].getBirdLabel());
                                
                                //새로운 사냥하면 안되는 새 인덱스 설정
                                this.randNum = (int)(Math.random() * birds.length);
                                //사냥하면 안되는 새 스레드 깨움
                                noHuntThread.notify();
                                noHuntThread.setRandNum(randNum);
                                birds[randNum].setIsAlive(false);
                                repaint();
                                //폭탄의 확률과 위치도 초기화
                                bombRandNum = (int)(Math.random()*100+1);
                                //사냥이 끝났으면 bomb=null로 초기화해서 사용자 입력창에서 조건으로 사용
                                bomb=null;
                            }
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }
}



