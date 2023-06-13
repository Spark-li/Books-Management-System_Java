package bookBms;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
public class Register {
	//管理员注册码
	private String adminRegistePassword="123456";
	private Connection conn=null;
	private Statement stmt=null;
	private ResultSet rs=null;
	public Register(JPanel AllPage,CardLayout cardLayout,Color color) {
		// TODO Auto-generated constructor stub
		/*
		 * 设计
		 * 整体一个JPanel register_page，加载到主面板中作为注册卡片
		 * register_page为表格布局，上方为标题，左右不使用，下方为frame固定不变
		 * 中间分行9行1列，8个JPanel组成中间JPanel加入到register_page中,1行空着占位
		 * 
		 */
		JPanel register_page=new JPanel(new GridLayout(2,1));
		register_page.setBackground(color);
		
		//title
		JLabel title=new JLabel("图书信息管理系统注册页面",JLabel.CENTER);
		title.setFont(new Font("楷体",Font.BOLD,30));
		title.setPreferredSize(new Dimension(100,200));
		register_page.add(title);
		
		
		//center table
		JPanel center=new JPanel(new GridLayout(9,1));
		center.setBackground(color);
		
		//id行
		JPanel p1=new JPanel();
		p1.setBackground(color);
		JLabel label_id=new JLabel("      账  号",JLabel.CENTER);
		label_id.setFont(new Font("黑体",0,20));
		JTextField text_id=new JTextField(20);
		text_id.setPreferredSize(new Dimension(0,25));
		p1.add(label_id);
		p1.add(text_id);
		center.add(p1);
		
		//name行
		JPanel p2=new JPanel();
		p2.setBackground(color);
		JLabel label_name=new JLabel("      用户名",JLabel.CENTER);
		label_name.setFont(new Font("黑体",0,20));
		JTextField text_name=new JTextField(20);
		text_name.setPreferredSize(new Dimension(0,25));
		p2.add(label_name);
		p2.add(text_name);
		center.add(p2);
		
		//sex行
		JPanel p3=new JPanel();
		p3.setBackground(color); 
		JLabel label_sex=new JLabel("    性  别         ",JLabel.CENTER);
		label_sex.setFont(new Font("黑体",0,20));
		JRadioButton m=new JRadioButton("男",true);
		m.setOpaque(false);
		m.setFont(new Font("黑体",0,18));
		JRadioButton w=new JRadioButton("女");
		w.setOpaque(false);
		w.setFont(new Font("黑体",0,18));
		ButtonGroup group_sex=new ButtonGroup();
		group_sex.add(m);
		group_sex.add(w);
		
		p3.add(label_sex);
		p3.add(m);
		p3.add(w);
		center.add(p3);
		
		//password行
		JPanel p4=new JPanel();
		p4.setBackground(color);
		JLabel label_pass=new JLabel("      密  码",JLabel.CENTER);
		label_pass.setFont(new Font("黑体",0,20));
		JPasswordField  pf_pass=new JPasswordField(20);
		pf_pass.setPreferredSize(new Dimension(0,25));
		p4.add(label_pass);
		p4.add(pf_pass);
		center.add(p4);
		
		//card行
		JPanel p5=new JPanel();
		p5.setBackground(color);
		JLabel label_card=new JLabel("    身份证号",JLabel.CENTER);
		label_card.setFont(new Font("黑体",0,20));
		JTextField text_card=new JTextField(20);
		text_card.setPreferredSize(new Dimension(0,25));
		p5.add(label_card);
		p5.add(text_card);
		center.add(p5);
		
		
		//先创建管理员授权码框行，根据注册身份设定可用性
		JPanel p7=new JPanel();
		p7.setBackground(color);
		JLabel label_author=new JLabel("管理员注册码",JLabel.CENTER);
		label_author.setFont(new Font("黑体",0,20));
		JPasswordField pf_author=new JPasswordField(20);
		pf_author.setPreferredSize(new Dimension(0,25));
		p7.add(label_author);
		p7.add(pf_author);
		
		
		
		//kind行
		JPanel p6=new JPanel();
		p6.setBackground(color);
		JLabel label_kind=new JLabel("    注册身份      ",JLabel.CENTER);
		label_kind.setFont(new Font("黑体",0,20));

		JRadioButton kind_admin=new JRadioButton("管理员",true);
		kind_admin.setFont(new Font("黑体",0,18));
		kind_admin.setOpaque(false);
		
		ActionListener JR =new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//判断触发监听的对象是否是admin框，授权码只有选择admin时可用
				if(((JRadioButton)e.getSource())==kind_admin)
				{
					pf_author.setEnabled(true);
				}
				else
				{
					//选择读者时清空密码框并关闭状态
					pf_author.setText(null);
					pf_author.setEnabled(false);
				}
			}
		};
		//加载监听
		kind_admin.addActionListener(JR);
		
		JRadioButton kind_reader=new JRadioButton("读者");
		kind_reader.setFont(new Font("黑体",0,18));
		kind_reader.setOpaque(false);
		//加载监听
		kind_reader.addActionListener(JR);
		ButtonGroup group_kind=new ButtonGroup();
		group_kind.add(kind_admin);
		group_kind.add(kind_reader);
		p6.add(label_kind);
		p6.add(kind_admin);
		p6.add(kind_reader);
		center.add(p6);
		
		//授权码行加载
		center.add(p7);
		
		
		ActionListener bSubmit=new ActionListener() {
			private String table;
			@Override
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(kind_admin.isSelected())
					table="admin";
				else
					table="reader";
				
				//先校验数据是否完整
				if(checkEmpty(text_name, text_id, text_card, pf_pass))
				{
					JOptionPane.showMessageDialog(null,"存在数据为空值");
				}
				else
				{
					try {
						//创建sql连接
						getStatement();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
					if(table.equals("admin"))
					{
						//验证注册码
						if(new String(pf_author.getPassword()).equals(adminRegistePassword))
						{
							try {
								
								

								//调用存储过程完成管理员注册并返回登录页
								String sql="call insert_admin_data('"+text_id.getText()+"','"+text_name.getText()+"','"+new String(pf_pass.getPassword())+"')";
								stmt.executeUpdate(sql);
								JOptionPane.showMessageDialog(null,"注册成功,即将返回登录页");
								cardLayout.show(AllPage, "login_page");
								
							} catch (Exception e1) {
								//抛异常则id重复
								text_id.setText(null);
								JOptionPane.showMessageDialog(null, "管理员id已被注册");
							}
							
						}
						else
						{
							pf_author.setText(null);
							JOptionPane.showMessageDialog(null,"授权码错误");
						}
					}
					//注册读者
					else 
					{
						//检查id是否存在，异常处理身份证，此处利用存储过程验证id
						String sql="call check_reader_id('"+text_id.getText()+"')";
						try {
							rs=stmt.executeQuery(sql);
							if(rs.next())
							{
								text_id.setText(null);
								JOptionPane.showMessageDialog(null, "读者id已被注册");
							}
							else
							{
								String sex="男";
								if(w.isSelected())
									sex="女";
								sql="call insert_reader_data('"+text_id.getText()+"','"+text_name.getText()+"','"+sex+"','"+new String(pf_pass.getPassword())+"','"+text_card.getText()+"')";

								stmt.executeUpdate(sql);
								JOptionPane.showMessageDialog(null,"注册成功,即将返回登录页");
								cardLayout.show(AllPage, "login_page");
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							//身份证验证触发器返回，到达异常则身份证验证失败
							text_card.setText(null);
							JOptionPane.showMessageDialog(null, "身份证无效或重复");
						}
						
					}	
				}
				try {
					closeSql();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
			}
		};
		ActionListener bBack=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//切换到登录页
				cardLayout.show(AllPage, "login_page");
			}
		};
		//提交,注册行
		JPanel p8=new JPanel();
		p8.setBackground(color);
		JButton b_submit=new JButton("提交");
		b_submit.setContentAreaFilled(false);
		b_submit.addActionListener(bSubmit);
		JButton b_back=new JButton("返回登录页");
		b_back.setContentAreaFilled(false);
		b_back.addActionListener(bBack);
		p8.add(b_submit);
		p8.add(b_back);
		center.add(p8);
		
		register_page.add(center);
		//加载进卡片组
		AllPage.add(register_page,"register_page");
		
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
	public boolean checkEmpty(JTextField j1,JTextField j2,JTextField j3,JPasswordField jp1)
	{
		if(j1.getText().equals("") || j2.getText().equals("") || new String(jp1.getPassword()).equals("")|| j3.equals(""))
			return true;
		return false;
		
	}
	

}
