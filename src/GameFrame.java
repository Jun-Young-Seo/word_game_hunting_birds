import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//초기 메인메뉴 화면 클래스
public class GameFrame extends JFrame {
    private JButton gameStart;
    private JButton exit;
    private JButton addWord;
    private GamePanel gamePanel = null;
    private JPanel menuPanel;
    private AddWordPanel addWordPanel=null;
    ImageIcon sky = new ImageIcon("images/sky.png"); //배경화면
    private GameFrame gameFrame = this;
    public GameFrame(){
        menuPanel=new menuPanel();
        setTitle("Word Bird Hunting Game");
        setSize(1920,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(menuPanel);
        setVisible(true);
    }


    //메뉴 출력 패널
    class menuPanel extends JPanel {
        //배경화면 그리기
        //오버라이딩으로 컴포넌트 그릴 때 마다 배경 그리기
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Image skyImg = sky.getImage();
            g.drawImage(skyImg,0,0,this.getWidth(),this.getHeight(),this);
        }

        public menuPanel() {
            //예쁜 배치를 위해 배치관리자 삭제
            setLayout(null);

            gameStart = new JButton("게임 시작");
            addWord = new JButton("단어 추가");
            exit = new JButton("나가기");

            //게임시작 버튼 클릭 시 작동
            gameStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getContentPane().removeAll(); // 기존 컴포넌트 제거
                    gamePanel = new GamePanel(gameFrame);
                    getContentPane().add(gamePanel, BorderLayout.CENTER);
                    getContentPane().revalidate();
                    getContentPane().repaint();
                }
            });
            //단어 추가 버튼 클릭 시 작동
            addWord.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getContentPane().removeAll();//기존 컴포넌트 제거
                    addWordPanel = new AddWordPanel(gameFrame);
                    getContentPane().add(addWordPanel);
                    getContentPane().revalidate();
                    getContentPane().repaint();
                }
            });
            //나가기 버튼 클릭시 작동
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            gameStart.setSize(200,50);
            exit.setSize(200,50);
            gameStart.setLocation(860,600);
            addWord.setSize(200,50);
            addWord.setLocation(860,675);
            exit.setLocation(860,750);
            add(gameStart);
            add(addWord);
            add(exit);
        }
    }

    //다른 클래스에서 호출돼서 메인 메뉴로 돌아오게 하는 함수
    public void showMainMenu() {
        getContentPane().removeAll();
        getContentPane().add(menuPanel, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}
