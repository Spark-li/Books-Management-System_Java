package bookBms;

import java.awt.*;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class Control_admin {
	private Connection conn=null;
	private Statement stmt=null;
	private ResultSet rs=null;
	//设定最多200条数据
	private int MAX_ROW=200;
	private DefaultTableModel model=null;
	
	
	public  Control_admin(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		/*
		 * 两行一列，标题和中间的面板
		 * 面板包括四个按钮
		 * 读者管理、图书管理、日志管理、退出登录
		 */
		JPanel admin_page=new JPanel(new GridLayout(2,1));
		admin_page.setBackground(color);
		//标题
		JLabel title =new JLabel("管理员操作主界面",JLabel.CENTER);
		title.setFont(new Font("楷体",0,50));
		admin_page.add(title);
		
		ActionListener raderAction =new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				manage_r(AllPage, cardLayout, color, user);
				cardLayout.show(AllPage, "admin_reader_page");
			}
		};

		
		ActionListener bookAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				manage_b(AllPage, cardLayout, color, user);
				cardLayout.show(AllPage, "admin_book_page");
			}
		};
		
		ActionListener logAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				manage_l(AllPage, cardLayout, color, user);
				cardLayout.show(AllPage, "admin_log_page");
			}
		};
		
		ActionListener quitAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "login_page");
			}
		};
		JPanel center=new JPanel(new GridLayout(2,2));
		center.setBackground(color);
		//读者管理
		JButton manage_reader=new JButton("读者管理");
		manage_reader.addActionListener(raderAction);
		manage_reader.setBackground(new Color(240,230,190));
		manage_reader.setFont(new Font("黑体",1,20));
		manage_reader.setBounds(new Rectangle(0,0,30,30));
		center.add(manage_reader);
		//图书管理
		JButton manage_book=new JButton("图书管理");
		manage_book.addActionListener(bookAction);
		manage_book.setBackground(new Color(200,220,180));
		manage_book.setFont(new Font("黑体",1,20));
		manage_book.setBounds(new Rectangle(0,0,30,30));
		center.add(manage_book);
		//日志管理
		JButton manage_log=new JButton("日志查询");
		manage_log.addActionListener(logAction);
		manage_log.setBackground(new Color(230,220,190));
		manage_log.setFont(new Font("黑体",1,20));
		manage_log.setBounds(new Rectangle(0,0,30,30));
		center.add(manage_log);
		
		//退出登录
		
		JButton back_login=new JButton("退出登录");
		back_login.addActionListener(quitAction);
		back_login.setBackground(new Color(240,220,190));
		back_login.setFont(new Font("黑体",1,20));
		back_login.setBounds(new Rectangle(0,0,30,30));
		center.add(back_login);
		
		
		admin_page.add(center);
		
		AllPage.add(admin_page,"admin_page");
		
	}
	public void manage_r(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		JPanel admin_reader_page=new JPanel(new GridLayout(3,1));
		admin_reader_page.setBackground(color);
		
		/*
		 * 增删改操作直接在表格中进行
		 */
		
		String[] table_title= {"ID","姓名","性别","密码","身份证号"};
		Object[][] table_frame=new Object[MAX_ROW][table_title.length];
		
		initTableData(table_frame, table_title,"reader");
		
		//组合标题和查询行
			JPanel mr_first=new JPanel();
			mr_first.setBackground(color);
			//title
			JLabel title =new JLabel("读者管理界面",JLabel.CENTER);
			title.setFont(new Font("楷体",0,40));
			
			
			//查询行
			JPanel mr_search=new JPanel(new GridLayout(1,4));
			mr_search.setBackground(color);
			JLabel reader_id = new JLabel("ID",JLabel.CENTER);
			JTextField text_id=new JTextField();
			
			JLabel reader_name=new JLabel("姓名",JLabel.CENTER);
			JTextField text_name=new JTextField();
			
			JLabel reader_sex=new JLabel("性别",JLabel.CENTER);
			JTextField text_sex=new JTextField();
			
			JButton reader_search=new JButton("查询");
			reader_search.setContentAreaFilled(false);
			
			
			model=new DefaultTableModel(table_frame,table_title) ;
			
			JTable table=new JTable(model);
			//滚动面板
			JScrollPane scrollpane=new JScrollPane(table);
			
			
			//查询按键监听
			ActionListener searchAction = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String sql="call search_reader('"+text_id.getText()+"','"+text_name.getText()+"','"+text_sex.getText()+"')";
					searchButton(sql, table_frame, table_title, table);
					text_id.setText(null);
					text_name.setText(null);
					text_sex.setText(null);
				}
			};
			//加载按键监听
			reader_search.addActionListener(searchAction);
			
			//监听表格单元格数据更改
			TableModelListener listener=new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent e) {
					// TODO Auto-generated method stub
					String []pair={"id","name","sex","password","card"};
					int row = e.getFirstRow();
					int column = e.getColumn();
					//获取修改后的新值
					Object data = table.getValueAt(row, column);
					//数组中是旧数据，当为null时则代表是在新增数据
					if(table_frame[row][0]==null)
					{
						JOptionPane.showMessageDialog(null, "不可新增数据，请前往注册面板");
						return;
					}
					try {
						getStatement();
						String sql="update reader set "+pair[column]+"='"+data+"' where id='"+table_frame[row][0]+"'";
						//System.out.println(sql);
						stmt.executeUpdate(sql);
						//同步更新数组中的数据
						table_frame[row][column]=data;
						JOptionPane.showMessageDialog(null, "修改成功");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//id重复或身份证验证错误抛出异常
						if(pair[column].equals("id"))
							JOptionPane.showMessageDialog(null,"修改的id已存在");
						else
							JOptionPane.showMessageDialog(null,"身份证无效或重复");
						model.setValueAt(table_frame[row][column], row, column);
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
			};
			//加载监听
			table.getModel().addTableModelListener(listener);
			
			
			
			ActionListener backAction = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					cardLayout.show(AllPage, "admin_page");
				}
			};
			
			ActionListener delAction = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//保存选择的行
					int[] rows=table.getSelectedRows();
					int sign=0;
					for(int i=0;i<rows.length;i++)
					{
						//选择的行有数据时进行验证是否可以删除
						if(table_frame[rows[i]][0]!=null)
						{
							try {
								getStatement();
								String sql="delete from reader where id='"+table_frame[rows[i]][0]+"'";
								System.out.println(sql);
								stmt.executeUpdate(sql);
								//没有抛异常则删除成功
								//表格中移除
								
								try {
									model.removeRow(rows[i]);
								}catch (Exception e3) {
									// TODO: handle exception
									//因为存在单元格监听，此处删除会抛出越界异常 但无关操作
								}
								//把所有要删除的行置空
								for(int k=0;k<table_frame[0].length;k++)
									table_frame[rows[i]][k]=null;
								sign++;
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								//抛异常则该用户存在书未归还
								JOptionPane.showMessageDialog(null,table_frame[rows[i]][1]+"存在书籍未归还");
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
					//存在成功删除的数据
					if(sign>0)
					{
						//双指针解决用户数据前移
						int i=rows[0];
						int j=i+1;
						while(i<table_frame.length && j<table_frame.length)
						{
							while(i<table_frame.length && table_frame[i][0]!=null)
								i++;
							while(j<table_frame.length && table_frame[j][0]==null)
								j++;
							if(i<table_frame.length && j<table_frame.length)
							{
								
								for(int k=0;k<table_frame[0].length;k++)
								{
									table_frame[i][k]=table_frame[j][k];
									table_frame[j][k]=null;
								}
								i++;j++;
							}
						}
						//全部成功删除
						if(sign==rows.length)
							JOptionPane.showMessageDialog(null,"删除成功");
						else
							JOptionPane.showMessageDialog(null,"部分删除成功");
						model=new DefaultTableModel(table_frame,table_title);
						table.setModel(model);
					}
					
				}
			};
			
			
			JPanel reader_button=new JPanel();
			reader_button.setBackground(color);
			JButton del=new JButton("删除");
			del.setContentAreaFilled(false);
			del.addActionListener(delAction);
			
			JButton back=new JButton("返回管理员主界面");
			back.setContentAreaFilled(false);
			back.addActionListener(backAction);
			
			reader_button.add(del);reader_button.add(back);
			
			mr_search.add(reader_id);
			mr_search.add(text_id);
			mr_search.add(reader_name);
			mr_search.add(text_name);
			mr_search.add(reader_sex);
			mr_search.add(text_sex);
			mr_search.add(reader_search);
			//加载作为第一区域
			mr_first.add(title);
			mr_first.add(mr_search);

			
			
			admin_reader_page.add(mr_first);
			admin_reader_page.add(scrollpane);

			admin_reader_page.add(reader_button);
		AllPage.add(admin_reader_page,"admin_reader_page");
		
	}
	public void manage_b(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{
		JPanel admin_book_page=new JPanel(new GridLayout(3,1));
		admin_book_page.setBackground(color);
		
		/*
		 * 删改操作直接在表格中进行,增加新开个面板
		 */
		
		
		String[] table_title= {"ID","ISBN","书名","作者","出版社","位置","已借阅","总数量"};
		Object[][] table_frame=new Object[MAX_ROW][table_title.length];
		
		initTableData(table_frame, table_title,"book");
		
		//组合标题和查询行
			JPanel mb_first=new JPanel();
			mb_first.setBackground(color);
			//title
			JLabel title =new JLabel("图书管理界面",JLabel.CENTER);
			title.setFont(new Font("楷体",0,40));
			
			
			//查询行
			JPanel mb_search=new JPanel(new GridLayout(1,4));
			mb_search.setBackground(color);
			JLabel book_name = new JLabel("书名",JLabel.CENTER);
			JTextField text_name=new JTextField();
			
			JLabel book_author=new JLabel("作者",JLabel.CENTER);
			JTextField text_author=new JTextField();
			
			JLabel book_press=new JLabel("出版社",JLabel.CENTER);
			JTextField text_press=new JTextField();
			
			JButton book_search=new JButton("查询");
			book_search.setContentAreaFilled(false);
			
			
			model=new DefaultTableModel(table_frame,table_title){
				private static final long serialVersionUID = 1L;
				//设置不可修改借阅数量
				public boolean isCellEditable(int row, int column) {
					if(column==6)
						return false;
					return true;
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
					String sql="call check_book('"+text_name.getText()+"','"+text_author.getText()+"','"+text_press.getText()+"')";
					searchButton(sql, table_frame, table_title, table);
					text_name.setText(null);
					text_author.setText(null);
					text_press.setText(null);
				}
			};
			//加载按键监听
			book_search.addActionListener(searchAction);
			
			//监听表格单元格数据更改
			TableModelListener listener=new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent e) {
					// TODO Auto-generated method stub
					String []pair={"id","isbn","book_name","author","press","location","borrow_num","num"};
					int row = e.getFirstRow();
					int column = e.getColumn();
					//获取修改后的新值
					Object data = table.getValueAt(row, column);
					//数组中是旧数据，当为null时则代表是在新增数据
					if(table_frame[row][0]==null)
					{
						JOptionPane.showMessageDialog(null, "新增数据请点击增加按钮");
						return;
					}
					try {
						getStatement();
						String sql="update book set "+pair[column]+"='"+data+"' where id='"+table_frame[row][0]+"'";
						//System.out.println(sql);
						//触发器只能在三个情况下通过，修改书的数量要大于借阅数量，修改的是位置，在这种书没有借阅的时候操作
						stmt.executeUpdate(sql);
						//同步更新数组中的数据
						table_frame[row][column]=data;
						JOptionPane.showMessageDialog(null, "修改成功");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "库存数量不能小于借阅数量、不能修改借阅数量且存在借阅时不能修改"+pair[column]);
						
						//还原数据
						model.setValueAt(table_frame[row][column], row, column);
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
			};
			//加载监听
			table.getModel().addTableModelListener(listener);
			
			
			
			ActionListener backAction = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					cardLayout.show(AllPage, "admin_page");
				}
			};
			ActionListener addAction = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					addBook(AllPage, cardLayout, color);
					cardLayout.show(AllPage, "addBook_page");
				}
			};
			ActionListener delAction = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//保存选择的行
					int[] rows=table.getSelectedRows();
					int sign=0;
					for(int i=0;i<rows.length;i++)
					{
						//选择的行有数据且借阅数量为0时可以删除
						if(table_frame[rows[i]][0]!=null)
						{
							try {
								getStatement();
								String sql="delete from book where id='"+table_frame[rows[i]][0]+"'";
								stmt.executeUpdate(sql);
								//没有抛异常则删除成功
								//表格中移除
								
								try {
									model.removeRow(rows[i]);
								}catch (Exception e3) {
									// TODO: handle exception
									//因为存在单元格监听，此处删除会抛出越界异常 但无关操作
								}
								//把所有要删除的行置空
								for(int k=0;k<table_frame[0].length;k++)
									table_frame[rows[i]][k]=null;
								sign++;
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								//抛异常则存在书未归还
								JOptionPane.showMessageDialog(null,table_frame[rows[i]][2]+"  存在未归还，全部归还后可删除");
							}finally
							{
								try {
									closeSql();
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									
								}
							}
						}
					}
					//当修改成功的数据>0时进行更新
					if(sign>0)
					{
						//双指针解决数据前移
						int i=rows[0];
						int j=i+1;
						while(i<table_frame.length && j<table_frame.length)
						{
							while(i<table_frame.length && table_frame[i][0]!=null)
								i++;
							while(j<table_frame.length && table_frame[j][0]==null)
								j++;
							if(i<table_frame.length && j<table_frame.length)
							{
								
								for(int k=0;k<table_frame[0].length;k++)
								{
									table_frame[i][k]=table_frame[j][k];
									table_frame[j][k]=null;
								}
								i++;j++;
							}
						}
						if(sign==rows.length)
							JOptionPane.showMessageDialog(null,"删除成功");
						else
							JOptionPane.showMessageDialog(null, "部分删除成功");
						model=new DefaultTableModel(table_frame,table_title);
						table.setModel(model);
					}
					
				}
			};
			JPanel book_button=new JPanel();
			book_button.setBackground(color);
			JButton add=new JButton("增加图书");
			add.setContentAreaFilled(false);
			add.addActionListener(addAction);
			
			JButton del=new JButton("删除");
			del.setContentAreaFilled(false);
			del.addActionListener(delAction);
			
			JButton back=new JButton("返回管理员主界面");
			back.setContentAreaFilled(false);
			back.addActionListener(backAction);
			
			book_button.add(add);book_button.add(del);book_button.add(back);
			
			mb_search.add(book_name);
			mb_search.add(text_name);
			mb_search.add(book_author);
			mb_search.add(text_author);
			mb_search.add(book_press);
			mb_search.add(text_press);
			mb_search.add(book_search);
			//加载作为第一区域
			mb_first.add(title);
			mb_first.add(mb_search);

			
			
			admin_book_page.add(mb_first);
			admin_book_page.add(scrollpane);

			admin_book_page.add(book_button);
		
		
		AllPage.add(admin_book_page,"admin_book_page");
		
	}
	public void manage_l(JPanel AllPage,CardLayout cardLayout,Color color,User user)
	{

		JPanel admin_log_page=new JPanel(new GridLayout(3,1));
		admin_log_page.setBackground(color);
		
		/*
		 * 此处只实现查询功能，日志是自动记录和删除的
		 */
		String[] table_title= {"ID","借阅人","借阅书籍","借阅时间","归还时间"};
		Object[][] table_frame=new Object[MAX_ROW][table_title.length];
		
		//加载所有数据
		initTableData(table_frame, table_title,"log_borrow");
		
		JPanel log_first=new JPanel();
		log_first.setBackground(color);
		//title
		JLabel title =new JLabel("日志查询界面 ",JLabel.CENTER);
		title.setFont(new Font("楷体",0,40));
		
		
		//查询行
		JPanel log_p=new JPanel(new GridLayout(1,4));
		log_p.setBackground(color);
		JLabel reader_id = new JLabel("借阅人id",JLabel.CENTER);
		JTextField text_id=new JTextField();
		
		JLabel book_name=new JLabel("书名",JLabel.CENTER);
		JTextField text_bname=new JTextField();
	
		JButton log_search=new JButton("查询");
		log_search.setContentAreaFilled(false);
		
		
		log_p.add(reader_id);
		log_p.add(text_id);
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
				String sql="call search_log('"+text_id.getText()+"','"+text_bname.getText()+"')";
				searchButton(sql, table_frame, table_title, table);
				text_id.setText(null);
				text_bname.setText(null);
				
			}
		};
		//加载按键监听
		log_search.addActionListener(searchAction);
		
		
		ActionListener backAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "admin_page");
			}
		};
		JPanel log_button=new JPanel();
		log_button.setBackground(color);
		JButton back=new JButton("返回管理员主界面");
		back.setContentAreaFilled(false);
		back.addActionListener(backAction);
		log_button.add(back);
		admin_log_page.add(log_first);
		admin_log_page.add(scrollpane);
		admin_log_page.add(log_button);
		AllPage.add(admin_log_page,"admin_log_page");
		
	}
	public void addBook(JPanel AllPage,CardLayout cardLayout,Color color)
	{
		JPanel add_page=new JPanel(new GridLayout(2,1));
		add_page.setBackground(color);
	
		//title
		JLabel title=new JLabel("图书入库界面",JLabel.CENTER);
		title.setFont(new Font("楷体",Font.BOLD,50));
		title.setPreferredSize(new Dimension(100,200));
		add_page.add(title);
		
		
		//data
		/*
		 * 8行一列 7行数据输入 1行按钮
		 */
		JPanel center=new JPanel(new GridLayout(8,1));
		center.setBackground(color);
		
		//id行
		JPanel p1=new JPanel();
		p1.setBackground(color);
		JLabel label_id=new JLabel("    编号",JLabel.CENTER);
		label_id.setFont(new Font("黑体",0,20));
		JTextField text_id=new JTextField(20);
		text_id.setPreferredSize(new Dimension(0,25));
		p1.add(label_id);
		p1.add(text_id);
		center.add(p1);
		
		//isbn
		JPanel p2=new JPanel();
		p2.setBackground(color);
		JLabel label_isbn=new JLabel("    isbn",JLabel.CENTER);
		label_isbn.setFont(new Font("黑体",0,20));
		JTextField text_isbn=new JTextField(20);
		text_isbn.setPreferredSize(new Dimension(0,25));
		p2.add(label_isbn);
		p2.add(text_isbn);
		center.add(p2);
		
		//book_name
		JPanel p3=new JPanel();
		p3.setBackground(color);
		JLabel label_name=new JLabel("    书名",JLabel.CENTER);
		label_name.setFont(new Font("黑体",0,20));
		JTextField text_name=new JTextField(20);
		text_name.setPreferredSize(new Dimension(0,25));
		p3.add(label_name);
		p3.add(text_name);
		center.add(p3);
		
		//author
		JPanel p4=new JPanel();
		p4.setBackground(color);
		JLabel label_author=new JLabel("    作者",JLabel.CENTER);
		label_author.setFont(new Font("黑体",0,20));
		JTextField text_author=new JTextField(20);
		text_author.setPreferredSize(new Dimension(0,25));
		p4.add(label_author);
		p4.add(text_author);
		center.add(p4);
		
		//出版社
		JPanel p5=new JPanel();
		p5.setBackground(color);
		JLabel label_press=new JLabel("  出版社",JLabel.CENTER);
		label_press.setFont(new Font("黑体",0,20));
		JTextField text_press=new JTextField(20);
		text_press.setPreferredSize(new Dimension(0,25));
		p5.add(label_press);
		p5.add(text_press);
		center.add(p5);
		
		//位置
		JPanel p6=new JPanel();
		p6.setBackground(color);
		JLabel label_location=new JLabel("存放位置",JLabel.CENTER);
		label_location.setFont(new Font("黑体",0,20));
		JTextField text_location=new JTextField(20);
		text_location.setPreferredSize(new Dimension(0,25));
		p6.add(label_location);
		p6.add(text_location);
		center.add(p6);
		
		//数量
		JPanel p7=new JPanel();
		p7.setBackground(color);
		JLabel label_num=new JLabel("入库数量",JLabel.CENTER);
		label_num.setFont(new Font("黑体",0,20));
		JTextField text_num=new JTextField(20);
		text_num.setPreferredSize(new Dimension(0,25));
		p7.add(label_num);
		p7.add(text_num);
		center.add(p7);
		
		//按钮行
		JPanel p8=new JPanel();
		p8.setBackground(color);
		JButton book_submit=new JButton("提交");
		book_submit.setContentAreaFilled(false);
		
		ActionListener submitAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					
					getStatement();
					String sql="insert into book values('"+text_id.getText()+"','"+text_isbn.getText()
					+"','"+text_name.getText()+"','"+text_author.getText()+"','"+text_press.getText()
					+"','"+text_location.getText()+"',0"+","+Integer.parseInt(text_num.getText())+")";
					//System.out.println(sql);
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "入库成功");
					text_id.setText(null);
					text_isbn.setText(null);
					text_name.setText(null);
					text_author.setText(null);
					text_press.setText(null);
					text_location.setText(null);
					text_num.setText(null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//id或ISBN存在重复
					JOptionPane.showMessageDialog(null, "id或isbn码存在重复");
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
		book_submit.addActionListener(submitAction);
		

		JButton back=new JButton("返回上层");
		back.setContentAreaFilled(false);
		
		ActionListener backAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "admin_book_page");
			}
		};
		back.addActionListener(backAction);
		p8.add(book_submit);
		p8.add(back);
		center.add(p1);
		center.add(p2);
		center.add(p3);
		center.add(p4);
		center.add(p5);
		center.add(p6);
		center.add(p7);
		center.add(p8);
		add_page.add(center);
		
		AllPage.add(add_page,"addBook_page");
		
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
