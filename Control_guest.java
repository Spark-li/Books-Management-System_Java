package bookBms;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Control_guest {
	private Connection conn=null;
	private Statement stmt=null;
	private ResultSet rs=null;
	//设定最多200条数据
	private int MAX_ROW=200;
	private DefaultTableModel model=null;
	
	public Control_guest(JPanel AllPage,CardLayout cardLayout,Color color) {
		
		/*
		 * 整体布局3行1列，第一行为查询行，第二行表格，显示查询数据,第三行退出按钮
		 * 查询行包含书名、作者、出版社 查询按钮
		 * 
		 */
		String[] table_title= {"ID","ISBN","书名","作者","出版社","位置","已借阅","总数量"};
		Object[][] table_frame=new Object[MAX_ROW][table_title.length];
		JPanel guest_page=new JPanel(new GridLayout(3,1));
		guest_page.setBackground(color);
		//初始化加载全部数据
		initTableData(table_frame, table_title,"book");
		//组合标题和查询行
		JPanel guest_first=new JPanel();
		guest_first.setBackground(color);
		//title
		JLabel title =new JLabel("临时访客查询界面",JLabel.CENTER);
		title.setFont(new Font("楷体",0,30));

		
		//查询行
		JPanel guest_search=new JPanel(new GridLayout(1,4));
		guest_search.setBackground(color);
		JLabel book_name = new JLabel("书名",JLabel.CENTER);
		JTextField text_name=new JTextField();
		
		JLabel book_author=new JLabel("作者",JLabel.CENTER);
		JTextField text_author=new JTextField();
		
		JLabel book_press=new JLabel("出版社",JLabel.CENTER);
		JTextField text_press=new JTextField();
		
		
		
		JButton book_search=new JButton("查询");
		book_search.setContentAreaFilled(false);
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
		book_search.addActionListener(search);
		
		
		ActionListener quitAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(AllPage, "login_page");
			}
		};
		JPanel p=new JPanel();
		p.setBackground(color);
		JButton quit=new JButton("退出登录");
		quit.addActionListener(quitAction);
		quit.setContentAreaFilled(false);
		p.add(quit);
		
		guest_search.add(book_name);
		guest_search.add(text_name);
		guest_search.add(book_author);
		guest_search.add(text_author);
		guest_search.add(book_press);
		guest_search.add(text_press);
		guest_search.add(book_search);
		//加载作为第一区域
		guest_first.add(title);
		guest_first.add(guest_search);
		
		guest_page.add(guest_first);
		//第二区域
		guest_page.add(scrollpane);
		guest_page.add(p);
		
		AllPage.add(guest_page,"guest_page");
		
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
