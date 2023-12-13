import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ScorePanel extends JPanel {
    private int round= 1;
    private String playerName;
    private int score;
    private ImageIcon heartImg=new ImageIcon("images/heart.png");
    private Vector<JLabel> hearts = new Vector<>(3);
    private JLabel scoreLabel = new JLabel(Integer.toString(score));
    public JLabel roundNumLabel = new JLabel(Integer.toString(round));
    private HashMap<String,ScoreRound> scoreRoundHashMap= new HashMap<>();
    // RGB 코드로 예쁜 색깔 설정
    Color bgcolor = new Color(255,218,185);
    
    //HashMap에 사용될 클래스
    //점수와 라운드를 묶어서 객체화
    protected class ScoreRound{
        private int score;
        private int round;

        public ScoreRound( int score, int round){
            this.score=score; this.round=round;
        }
    }
    public ScorePanel(){
        this.setBackground(bgcolor);
        try {
            //txt 파일의 각 라인을 모두 읽고
            Scanner scanner = new Scanner(new FileReader("source/ranking.txt"));
            while (scanner.hasNext()){
                StringTokenizer stk = new StringTokenizer(scanner.nextLine(),",");
                String name = stk.nextToken();
                int score = Integer.parseInt(stk.nextToken());
                int round = Integer.parseInt(stk.nextToken());
                ScoreRound s = new ScoreRound(score,round);
                //객체화 해서 <name,객체>쌍을 만들어 Hashmap에 모두 입력
                scoreRoundHashMap.put(name,s);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("can't open File!");
            System.exit(0);
        }
        RankingPanel rankingPanel = new RankingPanel();
        rankingPanel.setBounds(0,400,420,680);
        add(rankingPanel);
        setLayout(null);
        JLabel round = new JLabel("Round");
        JLabel score = new JLabel("점수 : ");
        scoreLabel.setFont(new Font("고딕체",Font.BOLD,30));
        score.setFont(new Font("고딕체",Font.BOLD,30));
        round.setFont(new Font("고딕체",Font.BOLD,50));
        roundNumLabel.setFont(new Font("고딕체",Font.BOLD,50));
        round.setSize(250,50);
        round.setLocation(50,30);
        score.setSize(100,50);
        scoreLabel.setSize(100,50);
        score.setLocation(100,100);
        scoreLabel.setLocation(200,100);
        roundNumLabel.setSize(50,50);
        roundNumLabel.setLocation(250,30);
        //목숨을 의미하는 하트 이미지 3개 생성
        //하트 이미지를 배열로 관리해서 오른쪽부터 편하게 삭제 가능
        for(int i=0;i<3;i++){
            hearts.add(new JLabel());
            hearts.get(i).setIcon(heartImg);
            hearts.get(i).setSize(120,120);
            hearts.get(i).setLocation(130*i,180);
            add(hearts.get(i));
        }
        add(score);
        add(scoreLabel);
        add(round);
        add(roundNumLabel);
    }
    //맞췄을 때
    //gameGround의 액션리스너가 호출
    public void increase(){
        score+=10;
        scoreLabel.setText(Integer.toString(score));
    }
    //틀렸을 때
    //gameGround의 액션리스너가 호출
    //틀렸으니까 하트도 하나 삭제
    public void decrease(){
        score-=20;
        if(score<0) score = 0;
        scoreLabel.setText(Integer.toString(score));
        lostHeart();
    }
    //하트를 모두 소진했을 경우를 검출
    public void lostHeart(){
        //만약 하트가 아직 남았다면
        if(hearts.size()>0) {
            //하트를 인덱스로 관리해서 오른쪽 하트부터 삭제
            this.remove(hearts.get(hearts.size() - 1));
            this.repaint();
            hearts.remove(hearts.size() - 1);
        }
    }
    //패배한 경우를 검출하는 함수
    //만약 남은 하트가 하나도 없는 경우 true 리턴
    public boolean isLose(){
        if(hearts.size()==0){
            return true;
        }
        else return false;
    }
    //현재 라운드 설정 함수
    //ranking.txt 입력 시 사용
    public void setRound(int round){
        this.round=round;
    }
    //게임 종료 후 gameGround의 사용자 이름 입력 창에서 호출하는 함수
    //ranking.txt 입력 시 사용
    public void setPlayerName(String playerName){
        this.playerName=playerName;
        //객체 생성 후
        ScoreRound sr = new ScoreRound(score,round);
        //HashMap에 입력
        scoreRoundHashMap.put(playerName,sr);
        //ranking.txt에 저장하는 함수
        saveScoresToFile();
    }
    //ranking.txt에 저장하는 함수
    public void saveScoresToFile() {
        try {
            FileWriter writer = new FileWriter("source/ranking.txt",true);
                writer.write(playerName + "," + score + "," + round + "\n"); // 파일에 쓰기
                writer.close();
        } catch (IOException e) {
            System.out.println("can't open File!");
            System.exit(0);
        }
    }
    public class RankingPanel extends JPanel {

        private ImageIcon goldImg = new ImageIcon("images/gold.png");
        private ImageIcon silverImg = new ImageIcon("images/silver.png");
        private ImageIcon bronzeImg = new ImageIcon("images/bronze.png");
        JLabel [] rankOne = new JLabel[3];
        JLabel [] rankTwo = new JLabel[3];
        JLabel [] rankThree = new JLabel[3];
        JLabel name = new JLabel("이름");
        JLabel score = new JLabel("점수");
        JLabel round = new JLabel("라운드");
        JLabel blank = new JLabel("");
        JLabel goldMedal = new JLabel();
        JLabel silverMedal = new JLabel();
        JLabel bronzeMedal = new JLabel();

        public RankingPanel() {
            this.setBackground(bgcolor);
            setLayout(null);
            goldMedal.setIcon(goldImg);
            silverMedal.setIcon(silverImg);
            bronzeMedal.setIcon(bronzeImg);
            goldMedal.setSize(100,100);
            silverMedal.setSize(100,100);
            bronzeMedal.setSize(100,100);
            name.setFont(new Font("고딕체",Font.BOLD,30));
            score.setFont(new Font("고딕체",Font.BOLD,30));
            round.setFont(new Font("고딕체",Font.BOLD,30));
            blank.setSize(100,30);
            name.setSize(100,30);
            score.setSize(100,30);
            round.setSize(100,30);
            blank.setLocation(0,0);
            name.setLocation(70,50);
            score.setLocation(180,50);
            round.setLocation(280,50);
            goldMedal.setLocation(0,100); //1등 금메달
            silverMedal.setLocation(0,200); // 2등 은메달
            bronzeMedal.setLocation(0,300);// 3등 동메달
            add(blank); add(name); add(score); add(round); add(goldMedal); add(silverMedal); add(bronzeMedal);
            for(int i=0;i<3;i++){
                rankOne[i] = new JLabel();
                rankTwo[i] = new JLabel();
                rankThree[i] = new JLabel();
            }
            //랭킹을 산정을 위해 scoreRoundHashMap의 키를 리스트로 변환
            List<String> keys = new ArrayList<>(scoreRoundHashMap.keySet());
            // 리스트를 사용자의 점수 순으로 내림차순으로 정렬
            keys.sort((k1, k2) -> Integer.compare(scoreRoundHashMap.get(k2).score, scoreRoundHashMap.get(k1).score));
            // 정렬된 리스트 순회
            // 1,2,3등만 사용
            for (int i = 0; i < keys.size(); i++) {
                String playerName = keys.get(i);
                ScoreRound s = scoreRoundHashMap.get(playerName);
                // 상위 3명에게 메달 할당
                if (i == 0) { // 1등
                    rankOne[0].setText(playerName);
                    rankOne[1].setText(String.valueOf(s.score));
                    rankOne[2].setText(String.valueOf(s.round));
                } else if (i == 1) { // 2등
                    rankTwo[0].setText(playerName);
                    rankTwo[1].setText(String.valueOf(s.score));
                    rankTwo[2].setText(String.valueOf(s.round));
                } else if (i == 2) { // 3등
                    rankThree[0].setText(playerName);
                    rankThree[1].setText(String.valueOf(s.score));
                    rankThree[2].setText(String.valueOf(s.round));
                }
            }
            for(int i=0;i<3;i++){
                rankOne[i].setSize(100,50);
                rankTwo[i].setSize(100,50);
                rankThree[i].setSize(100,50);
                rankOne[i].setFont(new Font("고딕체", Font.BOLD, 30));
                rankTwo[i].setFont(new Font("고딕체", Font.BOLD, 30));
                rankThree[i].setFont(new Font("고딕체", Font.BOLD, 30));
            }
            for(int i=0;i<3;i++){
                rankOne[i].setLocation(70 + 120*i,130);
                rankTwo[i].setLocation(70 + 120*i, 230);
                rankThree[i].setLocation(70+120*i,330);
                add(rankOne[i]);
                add(rankTwo[i]);
                add(rankThree[i]);
            }



        }
    }
}
