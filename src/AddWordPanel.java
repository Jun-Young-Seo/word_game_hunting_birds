import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
public class AddWordPanel extends JPanel {
    private JLabel title = new JLabel("단어 추가 메뉴");
    //사용자 이름 입력창 레퍼런스
    private JTextField input = new JTextField(30);
    //GampFrame의 showMainmeu() 호출을 위한 버튼
    private JButton backMenuPanel = new JButton("돌아가기");
    //단어 추가를 위한 FileWriter
    private FileWriter writer;
    //GampFrame의 showMainmeu() 호출을 위한 레퍼런스 변수
    private GameFrame gameFrame;
    //배경화면
    private ImageIcon sky = new ImageIcon("images/sky.jpg");
    private Image skyImg = sky.getImage();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(skyImg, 0, 0, this.getWidth(), this.getHeight(), this);
    }
    //GameFrame의 showMainmenu() 호출을 위해 생성자에서 GameFrame을 인자로 받음
    public AddWordPanel(GameFrame gameFrame){
        this.gameFrame=gameFrame;
        try {
            writer = new FileWriter("source/korean.txt",true);
        } catch (IOException e) { //예외처리
            System.out.println("can't open File! // 생성자 에러");
            System.exit(0);
        }

        setLayout(null);
        title.setSize(1000,200);
        title.setFont(new Font("고딕체",Font.BOLD,100));
        title.setForeground(Color.WHITE);
        title.setLocation(600,200);
        input.setSize(1000, 50);
        input.setFont(new Font("고딕체", Font.BOLD, 30));
        input.setLocation(450, 800);
        backMenuPanel.setSize(100,100);
        backMenuPanel.setLocation(900,600);
        input.addActionListener(new InputActionListener());
        backMenuPanel.addActionListener(new ActionListener() {
            //메인메뉴로 돌아가는 버튼을 클릭했을 때 호출
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //돌아갈 때 Writer 객체 close
                    writer.close();
                } catch (IOException event) {
                    System.out.println("can't open File! // 버튼에러");
                    System.exit(0);
                }
                //예외가 발생하지 않으면 메인메뉴로
                gameFrame.showMainMenu();
            }
        });
        add(title);
        add(input);
        add(backMenuPanel);
    }
    class InputActionListener implements ActionListener{
        //단어추가 필드에 단어를 입력 후 엔터를 누르면 호출
        @Override
        public void actionPerformed(ActionEvent e) {
            //입력창에서 단어를 읽어서
            String word =  input.getText();
            //txt 파일에 입력하고
            addWord(word);
            //입력창 초기화
            input.setText("");
        }
    }
    //korean.txt 파일에 단어를 추가하는 함수
    public void addWord(String word){
        try {
            writer.write(word + "\n"); // 단어 추가 후 줄바꿈
            writer.flush(); //버퍼 비워버리기
        } catch (IOException e) {
            System.out.println("can't open File! // addword 함수에러");
            System.exit(0);
        }

    }

    }

