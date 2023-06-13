package bookBms;
import java.awt.*;
import javax.swing.*;

public class menu {
	private JFrame frame=null;//基础窗体
	private CardLayout cardLayout=null;
	private JPanel page=null;//总卡片面板
	private Color backcolor=null;
	private User user=new User("",0);//默认为临时访客
	public menu() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		// TODO Auto-generated constructor stub
		frame=new JFrame("BOOKBMS");
		//窗体大小
		Rectangle rectangle=new Rectangle(400, 150, 800, 700);	
		frame.setBounds(rectangle);
		//窗体下方布局
		JLabel neg=new JLabel("2023-6 基于Java Swing及Java awt",JLabel.CENTER);
		neg.setFont(new Font("黑体",Font.BOLD,15));
		neg.setPreferredSize(new Dimension(100,100));
		frame.add(neg,BorderLayout.SOUTH);
		//整体颜色
		backcolor=new Color(245,245,220);
		frame.getContentPane().setBackground(backcolor);
		//默认关闭方式
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//卡片布局
		cardLayout=new CardLayout();
		//卡片面板
		page=new JPanel();
		page.setLayout(cardLayout);
		
		//创建登陆,注册面板
		new Login(page,cardLayout,backcolor,user);
		
		//创建控制面板
		new Control_admin(page, cardLayout, backcolor,user);
		new Control_reader(page, cardLayout, backcolor,user);
		new Control_guest(page, cardLayout, backcolor);
		
		
		//加载面板
		frame.add(page);
		
		frame.repaint();
		frame.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		new menu();
	}
}

