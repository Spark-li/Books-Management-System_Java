package bookBms;
import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class Login {

	private Connection conn=null;
	private Statement stmt=null;
	private ResultSet rs=null;
	
	public Login(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		
		JPanel login_page=new JPanel();
		//表格布局
		login_page.setLayout(new GridLayout(2,1));
		login_page.setBackground(color);
		
		//运行时对借阅日志进行重置
		try {
			getStatement();
			String sql="call log_reset()";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				closeSql();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//创建标题标签 上
		JLabel title=new JLabel("图书信息管理系统登录界面",JLabel.CENTER);
		title.setFont(new Font("楷体",Font.BOLD,30));
		title.setPreferredSize(new Dimension(100,200));
		//title.setBackground(color);
		
		//中
		JPanel center=new JPanel(new GridLayout(4,1));
		center.setBackground(color);
		//中1
		JPanel centerOne=new JPanel();
		JLabel userLabel = new JLabel("账号",JLabel.CENTER);
		JTextField userText = new JTextField(20);
		
		userLabel.setFont(new Font("黑体",0,25));
		userText.setPreferredSize(new Dimension(40,40));
		centerOne.add(userLabel);centerOne.add(userText);
		centerOne.setBackground(color);
		center.add(centerOne);
		
		//中2
		JPanel centerTwo=new JPanel();
		userLabel = new JLabel("密码",JLabel.CENTER);
		userLabel.setFont(new Font("黑体",0,25));
		JPasswordField passwordField=new JPasswordField(20);
		passwordField.setPreferredSize(new Dimension(40,40));
		centerTwo.add(userLabel);centerTwo.add(passwordField);
		centerTwo.setBackground(color);
		center.add(centerTwo);
		
		//中3
		JPanel centerTh=new JPanel();
		JRadioButton jRadioButtons1 = new JRadioButton("管理员");
		
        JRadioButton jRadioButtons2 = new JRadioButton("读者",true);
        JRadioButton jRadioButtons3 = new JRadioButton("临时访客");
        //背景透明
        jRadioButtons1.setOpaque(false);jRadioButtons2.setOpaque(false);jRadioButtons3.setOpaque(false);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButtons1);
        buttonGroup.add(jRadioButtons2);
        buttonGroup.add(jRadioButtons3);
        centerTh.add(jRadioButtons1); centerTh.add(jRadioButtons2); centerTh.add(jRadioButtons3);
        centerTh.setBackground(color);
        center.add(centerTh);
        
        //登录按钮监听器
        ActionListener LoginActionListener=new ActionListener() {
			
        	private String table;
        	//mysql
        	
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		// TODO Auto-generated method stub
        		//调用函数获得选择的账户类型
        		table=selectRadioButton(centerTh);
        			
        		try {
        			//访客不验证账号密码
        			if(table.equals("guest"))
        			{
        				user.setId("guest");
        				JOptionPane.showMessageDialog(null, "登录成功");
            			cardLayout.show(AllPage,"guest_page");
        			}
        			else
        				checkLogin();
        			//清空
        			userText.setText("");
        			passwordField.setText("");
        		} catch (Exception e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
        		
        		
        	}
        	public void checkLogin() throws Exception
        	{
        		getStatement();
        		String sql="call checkLogin('"+table+"','"+userText.getText()+"','"+new String(passwordField.getPassword())+"');";
        		rs=stmt.executeQuery(sql);
        		//查询此id的借阅数量并记录
    			
        		if(rs.next())
        		{
        			//管理员2级，读者1级
        			user.setGrade(table.equals("admin")?2:1);
        			//只有读者登录的时需要使用用户信息
        			if(user.getGrade()==1)
        			{
        				//保存登录用户的信息
            			user.setId(userText.getText());
            			user.setName(rs.getString("name"));
            			user.setSex(rs.getString("sex"));
            			user.setPassword(rs.getString("password"));
            			user.setCard(rs.getString("card"));
        			}
		
        			JOptionPane.showMessageDialog(null, "登录成功");
        			if(user.getGrade()==1)
        			{
        				cardLayout.show(AllPage,"reader_page");
        				//获取当前用户的借阅数量
        				getUserBorrow(user);
        				//System.out.println(user.getBorrow());
        				//System.out.println(user.getBorrow());
        			}
        				
        			else
        				cardLayout.show(AllPage, "admin_page");
        			userText.setText(null);
        			passwordField.setText(null);
        		}
        			
        		else
        		{
        			JOptionPane.showMessageDialog(null,"用户名不存在或密码错误，请重试");

        			
        		}
        		closeSql();
        		
        		
        		
        	}
        	
        	
        };
        
        
        //注册按钮监听器
        ActionListener RegisterActionListener=new ActionListener() {
			String table="";
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				table=selectRadioButton(centerTh);
				//清空账号和密码
				userText.setText(null);
				passwordField.setText(null);
				//创建注册面板
				new Register(AllPage,cardLayout,color);
				//显示注册面板
				cardLayout.show(AllPage, "register_page");
			}
		};
        //中4
        JPanel centerF=new JPanel();
        JButton b1=new JButton("登录");
        b1.setContentAreaFilled(false);
        //加载监听
        b1.addActionListener(LoginActionListener);
        
        JButton b2=new JButton("注册");
        b2.setContentAreaFilled(false);
        b2.addActionListener(RegisterActionListener);
        
        centerF.add(b1);centerF.add(b2);
        centerF.setBackground(color);
        center.add(centerF);
		//加入到窗体中
        login_page.add(title);
		login_page.add(center);

		//左右为空
		//设置内部颜色
		//加载login_page卡片，标注名字
		AllPage.add("login_page",login_page);
		
	}
	public String selectRadioButton(JPanel centerTh)
	{
		String table="";
		for(Component t:centerTh.getComponents())
		{
			if(((JRadioButton)t).isSelected())
			{
				table=((JRadioButton)t).getText();
				if(table.equals("管理员"))table="admin";
				else if(table.equals("读者"))table="reader";
				else
					table="guest";
			}
				
		}
		return table;
	}
	
	public void getStatement() throws Exception
	{
		String root="root";
		String pass="513254687";
		conn=DriverManager.getConnection("jdbc:mysql:///bookBms",root,pass);
		stmt=conn.createStatement();
	}
	public void closeSql() throws SQLException
	{
		if(conn!=null)
			conn.close();
		if(stmt!=null)
			stmt.close();
		if(rs!=null)
			rs.close();
	}
	public void getUserBorrow(User user)
	{
		try {
			getStatement();
			int n = 0;
			CallableStatement cs = conn.prepareCall("call sele_borrow(?,?)");
			cs.setString(1, user.getId());
			cs.registerOutParameter(2, Types.INTEGER);//注册一个输出参数
			cs.execute();//执行
			user.setBorrow(cs.getInt(2));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				closeSql();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}




