package bookBms;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Control_reader {

	private Connection conn=null;
	private Statement stmt=null;
	private ResultSet rs=null;
	//设定最多200条数据
	private int MAX_ROW=200;
	private DefaultTableModel model=null;
	//最多借阅10本
	private int MAX_BORROW=10;
	
	
	
	public Control_reader(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		/*
		 * 整体两行一列，一列标题，一列按钮面板
		 * 个人信息管理，图书查询及借阅，图书归还,退出登录
		 */
		
		
		
		JPanel reader_page=new JPanel(new GridLayout(2,1));
		reader_page.setBackground(color);
		
		//标题
		JLabel title =new JLabel("读者操作主界面",JLabel.CENTER);
		title.setFont(new Font("楷体",0,30));
		reader_page.add(title);
		
		ActionListener selfAction =new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				manage_s(AllPage, cardLayout, color, user);
				cardLayout.show(AllPage, "reader_self_page");
			}
		};

		
		ActionListener borrowAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				manage_borrow(AllPage, cardLayout, color, user);
				cardLayout.show(AllPage, "reader_borrow_page");
			}
		};
		
		ActionListener backAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				manage_back(AllPage, cardLayout, color, user);
				
				cardLayout.show(AllPage, "reader_back_page");
			}
		};
		ActionListener quitAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "login_page");
			}
		};
		//四个按钮2行2列排列
		JPanel center=new JPanel(new GridLayout(2,2));
		center.setBackground(color);
		//读者管理
		JButton manage_self=new JButton("个人信息修改");
		manage_self.addActionListener(selfAction);
		manage_self.setBackground(new Color(240,230,190));
		manage_self.setFont(new Font("黑体",1,20));
		manage_self.setBounds(new Rectangle(0,0,30,30));
		center.add(manage_self);
		//图书管理
		JButton manage_book=new JButton("图书借阅");
		manage_book.addActionListener(borrowAction);
		manage_book.setBackground(new Color(200,220,180));
		manage_book.setFont(new Font("黑体",1,20));
		manage_book.setBounds(new Rectangle(0,0,30,30));
		center.add(manage_book);
		//日志管理
		JButton back_book=new JButton("图书归还");
		back_book.addActionListener(backAction);
		back_book.setBackground(new Color(230,220,190));
		back_book.setFont(new Font("黑体",1,20));
		back_book.setBounds(new Rectangle(0,0,30,30));
		center.add(back_book);
		
		//退出登录
		
		JButton back_login=new JButton("退出登录");
		back_login.addActionListener(quitAction);
		back_login.setBackground(new Color(240,220,190));
		back_login.setFont(new Font("黑体",1,20));
		back_login.setBounds(new Rectangle(0,0,30,30));
		center.add(back_login);
		
		
		reader_page.add(center);
		
		

		
		AllPage.add(reader_page,"reader_page");
	}
	
	public void manage_s(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		/*
		 * 整体两行一列
		 *中间分 8行一列 
		 */
		JPanel reader_self_page=new JPanel(new GridLayout(2,1));
		reader_self_page.setBackground(color);
		
		//title
		JLabel title=new JLabel("个人信息修改界面",JLabel.CENTER);
		title.setFont(new Font("楷体",Font.BOLD,50));
		title.setPreferredSize(new Dimension(100,200));
		reader_self_page.add(title);
		
		
		//center
		JPanel center=new JPanel(new GridLayout(7,1));
		center.setBackground(color);
		
		//id行
		JPanel p1=new JPanel();
		p1.setBackground(color);
		JLabel label_id=new JLabel("      账  号",JLabel.CENTER);
		label_id.setFont(new Font("黑体",0,20));
		JTextField text_id=new JTextField(20);
		text_id.setPreferredSize(new Dimension(0,25));
		text_id.setText(user.getId());
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
		text_name.setText(user.getName());
		p2.add(label_name);
		p2.add(text_name);
		center.add(p2);
		
		//sex行
		JPanel p3=new JPanel();
		p3.setBackground(color); 
		JLabel label_sex=new JLabel("    性  别         ",JLabel.CENTER);
		label_sex.setFont(new Font("黑体",0,20));
		JRadioButton m=new JRadioButton("男");
		m.setOpaque(false);
		m.setFont(new Font("黑体",0,18));
		JRadioButton w=new JRadioButton("女");
		w.setOpaque(false);
		w.setFont(new Font("黑体",0,18));
		ButtonGroup group_sex=new ButtonGroup();
		group_sex.add(m);
		group_sex.add(w);
		if(user.getSex().equals("男"))
			m.setSelected(true);
		else
			w.setSelected(true);
		
		
		p3.add(label_sex);
		p3.add(m);
		p3.add(w);
		center.add(p3);
		
		//旧password行
		JPanel p4=new JPanel();
		p4.setBackground(color);
		JLabel label_oldpass=new JLabel("      旧密码",JLabel.CENTER);
		label_oldpass.setFont(new Font("黑体",0,20));
		JPasswordField  pf_oldpass=new JPasswordField(20);
		pf_oldpass.setPreferredSize(new Dimension(0,25));
		p4.add(label_oldpass);
		p4.add(pf_oldpass);
		center.add(p4);
		
		//新password行
		JPanel p5=new JPanel();
		p5.setBackground(color);
		JLabel label_newpass=new JLabel("      新密码",JLabel.CENTER);
		label_newpass.setFont(new Font("黑体",0,20));
		JPasswordField  pf_newpass=new JPasswordField(20);
		pf_newpass.setPreferredSize(new Dimension(0,25));
		p5.add(label_newpass);
		p5.add(pf_newpass);
		center.add(p5);
		
		
		//card行
		JPanel p6=new JPanel();
		p6.setBackground(color);
		JLabel label_card=new JLabel("    身份证号",JLabel.CENTER);
		label_card.setFont(new Font("黑体",0,20));
		JTextField text_card=new JTextField(20);
		text_card.setPreferredSize(new Dimension(0,25));
		text_card.setText(user.getCard());
		p6.add(label_card);
		p6.add(text_card);
		center.add(p6);
		
		ActionListener saveAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//修改了密码先验证旧密码正确性
				String opass=new String(pf_oldpass.getPassword());
				String npass=new String(pf_newpass.getPassword());
				User t=new User();//保存修改后的值
				t.setPassword(user.getPassword());
				//新旧密码不为空，验证旧密码正确
				if(!opass.equals("") && !npass.equals(""))
				{
					if(!opass.equals(user.getPassword()))
					{
						JOptionPane.showMessageDialog(null,"旧密码不正确");
						pf_oldpass.setText(null);
						return;
					}
					else
					{
						t.setPassword(npass);
					}
				}
				//新旧密码为空则不修改密码
				else if(opass.equals("") && npass.equals(""))
					;
				//新旧密码存在一个为空，数据错误
				else
				{
					JOptionPane.showMessageDialog(null,"旧密码或新密码未输入");
					pf_newpass.setText(null);
					pf_oldpass.setText(null);
					return;
				}
				//修改后的变量
				
				t.setId(text_id.getText());
				t.setName(text_name.getText());
				if(w.isSelected())
					t.setSex("女");
				else
					t.setSex("男");

				
					t.setCard(text_card.getText());
				
				try {
					getStatement();
					if(!user.getId().equals(t.getId()))
					{
						rs=stmt.executeQuery("call check_reader_id('"+t.getId()+"')");
						if(rs.next())
						{
							JOptionPane.showMessageDialog(null, "修改的id已存在");
							text_id.setText(user.getId());
							return;
						}
						
					}
					
					String sql="update reader set id="+"'"+t.getId()+"'"+",name="+"'"+t.getName()+"'"
					+",sex="+"'"+t.getSex()+"'"+",password="+"'"+t.getPassword()+"'"+",card="
					+"'"+t.getCard()+"' where id='"+user.getId()+"'";
//					System.out.println(sql);
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null,"保存成功");
					user.setAll(t);
					cardLayout.show(AllPage,"reader_page");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"身份证重复或格式不正确");
					text_card.setText(user.getCard());
				}finally {
					try {
						closeSql();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				
			}
		};
		
		ActionListener backAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "reader_page");
			}
		};
		//按钮行
		JPanel self_button=new JPanel();
		self_button.setBackground(color);
		
		JButton save =new JButton("保存");
		save.setContentAreaFilled(false);
		save.addActionListener(saveAction);
		
		JButton back = new JButton("返回读者主界面");
		back.setContentAreaFilled(false);
		back.addActionListener(backAction);
		
		
		self_button.add(save);
		self_button.add(back);
		center.add(self_button);
		reader_self_page.add(center);
		
		AllPage.add(reader_self_page,"reader_self_page");
	}
	public void manage_borrow(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		JPanel reader_borrow_page=new JPanel(new GridLayout(3,1));
		reader_borrow_page.setBackground(color);
		
		String[] table_title= {"ID","ISBN","书名","作者","出版社","位置","已借阅","总数量"};
		Object[][] table_frame=new Object[MAX_ROW][table_title.length];
		
		//初始化加载全部数据
		initTableData(table_frame, table_title,"book");
		//组合标题和查询行
		JPanel reader_first=new JPanel();
		reader_first.setBackground(color);
		//title
		JLabel title =new JLabel("临时访客查询界面",JLabel.CENTER);
		title.setFont(new Font("楷体",0,30));

		
		//查询行
		JPanel book_search=new JPanel(new GridLayout(1,4));
		book_search.setBackground(color);
		JLabel book_name = new JLabel("书名",JLabel.CENTER);
		JTextField text_name=new JTextField();
		
		JLabel book_author=new JLabel("作者",JLabel.CENTER);
		JTextField text_author=new JTextField();
		
		JLabel book_press=new JLabel("出版社",JLabel.CENTER);
		JTextField text_press=new JTextField();
		
		
		
		JButton button_search=new JButton("查询");
		button_search.setContentAreaFilled(false);
		//设置表格内部不能编辑
		model=new DefaultTableModel(table_frame,table_title) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
				}
		};
		JTable table=new JTable(model);
		//滚动面板
		JScrollPane scrollpane=new JScrollPane(table);
		
		//查询按钮事件监听
		ActionListener search=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sql="call check_book('"+text_name.getText()+"','"+text_author.getText()+"','"+text_press.getText()+"')";
				searchButton(sql, table_frame, table_title, table);
				//清空输入数据
				text_name.setText(null);
				text_author.setText(null);
				text_press.setText(null);
				
				
				
			}
		};
		//按钮加载监听
		button_search.addActionListener(search);
		
		JPanel p=new JPanel();
		p.setBackground(color);
		
		JButton borrow=new JButton("借阅");
		borrow.setContentAreaFilled(false);
		
		ActionListener borrowAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[]=table.getSelectedRows();
				//个人借阅书籍数量超过最大值禁止借阅
				if(rows.length+user.getBorrow()>MAX_BORROW)
				{
					JOptionPane.showMessageDialog(null,"借阅数量超过最大借阅数量");
					return;
				}
				
				for(int i=0;i<rows.length;i++)
				{
					//选择的行非空行
					if(table_frame[rows[i]][0]!=null)
					{
						try {
							
							getStatement();
							//修改book表
							String sql="call update_bookBorrow("+(Integer.parseInt(table_frame[rows[i]][6].toString())+1)+",'"+table_frame[rows[i]][0]+"')";
							stmt.executeUpdate(sql);
							//修改reader_borrow表
							sql="call update_borrow("+user.getBorrow()+",'"+user.getId()+"')";
							stmt.executeUpdate(sql);
							//增加log_borrow
							sql="call insert_log('"+user.getId()+"','"+table_frame[rows[i]][2]+"')";
							stmt.executeUpdate(sql);
							JOptionPane.showMessageDialog(null,table_frame[rows[i]][2]+ "借阅成功");

							user.setBorrow(user.getBorrow()+1);
							table_frame[rows[i]][6]=Integer.parseInt(table_frame[rows[i]][6].toString())+1;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, table_frame[rows[i]][2]+"已无库存");
						}finally {
							try {
								closeSql();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}
				}
				model=new DefaultTableModel(table_frame,table_title);
				table.setModel(model);
				
			}
		};
		borrow.addActionListener(borrowAction);
		JButton back=new JButton("返回读者主界面");
		back.setContentAreaFilled(false);
		p.add(borrow);
		p.add(back);
		ActionListener backAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "reader_page");
				
			}
		};
		
		back.addActionListener(backAction);
		
		
		book_search.add(book_name);
		book_search.add(text_name);
		book_search.add(book_author);
		book_search.add(text_author);
		book_search.add(book_press);
		book_search.add(text_press);
		book_search.add(button_search);
		//加载作为第一区域
		reader_first.add(title);
		reader_first.add(book_search);
		
		reader_borrow_page.add(reader_first);
		//第二区域
		reader_borrow_page.add(scrollpane);
		reader_borrow_page.add(p);
		
		
		AllPage.add(reader_borrow_page,"reader_borrow_page");
	}
	public void manage_back(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		JPanel reader_back_page=new JPanel(new GridLayout(3,1));
		reader_back_page.setBackground(color);
		
		String[] table_title= {"ID","借阅人","借阅书籍","借阅时间","归还时间"};
		Object[][] table_frame=new Object[MAX_ROW][table_title.length];
		
		initTableData(table_frame, table_title, "log_borrow", user);
		
		JPanel log_first=new JPanel();
		log_first.setBackground(color);
		//title
		JLabel title =new JLabel("读者书籍归还界面 ",JLabel.CENTER);
		title.setFont(new Font("楷体",0,40));
		
		
		//查询行
		JPanel log_p=new JPanel(new GridLayout(1,4));
		log_p.setBackground(color);

		
		JLabel book_name=new JLabel("书名",JLabel.CENTER);
		JTextField text_bname=new JTextField();
	
		JButton log_search=new JButton("查询");
		log_search.setContentAreaFilled(false);
		
		
		log_p.add(book_name);
		log_p.add(text_bname);
		log_p.add(log_search);
		
		log_first.add(title);
		log_first.add(log_p);
		
		model=new DefaultTableModel(table_frame,table_title) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
				}
		};
		JTable table=new JTable(model);
		//滚动面板
		JScrollPane scrollpane=new JScrollPane(table);
		//查询按键监听
		ActionListener searchAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sql="select * from log_borrow where user_id='"+user.getId()+"' and book_name='"+text_bname.getText()+"'";
				System.out.println(sql);
				searchButton(sql, table_frame, table_title, table);
				text_bname.setText(null);
				
			}
		};
		//加载按键监听
		log_search.addActionListener(searchAction);
		
		JPanel log_button=new JPanel();
		log_button.setBackground(color);
		
		JButton book_back=new JButton("归还");
		book_back.setContentAreaFilled(false);
		
		ActionListener bookBackAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[]=table.getSelectedRows();
				for(int i=0;i<rows.length;i++)
				{
					if(table_frame[rows[i]][0]!=null)
					{
						try {
							getStatement();
							//更新日志
							String sql="call update_log('"+table_frame[rows[i]][0]+"')";
							stmt.executeUpdate(sql);
							
							//减少此用户借阅数量
							user.setBorrow(user.getBorrow()-1);
							System.out.println(user.getBorrow());
							sql="call update_borrow("+user.getBorrow()+",'"+user.getId()+"')";
							//System.out.println(sql);
							stmt.executeUpdate(sql);
							//增加图书库存
							sql="update book set borrow_num=borrow_num-1 where book_name='"+table_frame[rows[i]][2]+"'";
							stmt.executeUpdate(sql);
							JOptionPane.showMessageDialog(null, "归还成功");
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}finally {
							try {
								closeSql();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
				}
				tableReset(table_frame);
				initTableData(table_frame, table_title, "log_borrow",user);
				model=new DefaultTableModel(table_frame,table_title);
				table.setModel(model);
				
			}
		};
		
		book_back.addActionListener(bookBackAction);
		
		ActionListener backAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "reader_page");
			}
		};
		
		JButton back=new JButton("返回管理员主界面");
		back.setContentAreaFilled(false);
		back.addActionListener(backAction);
		log_button.add(book_back);
		log_button.add(back);
		reader_back_page.add(log_first);
		reader_back_page.add(scrollpane);
		reader_back_page.add(log_button);
		
		
		
		AllPage.add(reader_back_page,"reader_back_page");
	}

	

	
	
	public void getStatement() throws Exception
	{
		String root="root";
		String pass="";
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
	public void tableReset(Object table[][])
	{
		for(int i=0;i<MAX_ROW;i++)
		{
			for(int j=0;j<table[0].length;j++)
				table[i][j]=null;
		}
	}
	public void initTableData(Object table[][],String[] table_title,String name)
	{
		try {
			getStatement();
			String sql="select * from "+name;
			rs=stmt.executeQuery(sql);
			int i=0;
			while(rs.next())
			{
					for(int j=0;j<table_title.length;j++)
						table[i][j]=rs.getString(j+1);
					i++;
			}
			
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
			}finally
			{
				try {
					closeSql();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void initTableData(Object table[][],String[] table_title,String name,User user)
	{
		try {
			getStatement();
			String sql="select * from "+name+" where user_id='"+user.getId()+"' and end_time is null";
			
			rs=stmt.executeQuery(sql);
			int i=0;
			while(rs.next())
			{
					for(int j=0;j<table_title.length;j++)
						table[i][j]=rs.getString(j+1);
					i++;
			}
			
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
			}finally
			{
				try {
					closeSql();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void searchButton(String sql,Object[][]table_frame,String[]table_title,JTable table)
	{
		try {
			getStatement();
			
			rs=stmt.executeQuery(sql);
			tableReset(table_frame);
			int i=0;
			while(rs.next())
			{
				for(int j=0;j<table_title.length;j++)
					table_frame[i][j]=rs.getString(j+1);
				i++;
			}
			//重新加载数据，并清空查询栏
			model=new DefaultTableModel(table_frame,table_title);
			table.setModel(model);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally
		{
			try {
				closeSql();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	
}
