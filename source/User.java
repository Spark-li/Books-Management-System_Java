package bookBms;

public class User {
	private String id;
	private String name;
	private String sex;
	private String password;
	private String card;
	private int grade;
	private int borrow;
	public User() {
		// TODO Auto-generated constructor stub
	}
	public void setAll(User t)
	{
		id=t.id;
		name=t.name;
		sex=t.sex;
		password=t.password;
		card=t.card;
	}
	public User(String id,int grade)
	{
		this.id=id;
		this.grade=grade;
	}
	public int getGrade() {
		return grade;
	}
	 public String getId() {
		return id;
	}
	 public void setGrade(int grade) {
		this.grade = grade;
	}
	 public void setId(String id) {
		this.id = id;
	}
	public User(String id, String name, String sex, String password, String card, int grade) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.password = password;
		this.card = card;
		this.grade = grade;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public int getBorrow() {
		return borrow;
	}
	public void setBorrow(int borrow) {
		this.borrow = borrow;
	}
	
	 
}
