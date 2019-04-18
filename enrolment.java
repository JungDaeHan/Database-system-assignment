import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class enrolment{

	static final String dbURL = "jdbc:mysql://localhost:3306/enrolment";
	static final String dbURL2 = "jdbc:mysql://localhost:3306/stu_info";
	static final String username = "root";
	static final String password = "cc1534269262";
	static Statement stmt = null;
	static Statement stmt_info = null;
	static Connection conn = null;
	static Connection conn_info = null;
	static ResultSet rs;
	static ResultSet rs_info;
	static Scanner s = new Scanner(System.in);
	static String sql = "null";
	static String sql_info = "null";
	static int i,j;
	
	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException {
		// TODO Auto-generated method stub

		try{
			
			conn = DriverManager.getConnection(dbURL, username, password);
			
			
			if(conn != null){
				;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		try{
			
			conn_info = DriverManager.getConnection(dbURL2, username, password);
			
			if(conn_info != null){
				;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		stmt = conn.createStatement();
		stmt_info = conn_info.createStatement();
		
		String student_id = log_In();
		String enroll_Num = "";
		
		if(student_id.equals("9999"))
		{
			while(true)
			{
				System.out.println("0.종료");
				System.out.println("1.설강하기");
				System.out.println("2.폐강하기");
				System.out.println("3.학부별 인원수 통계");
				int select = s.nextInt();
				switch(select)
				{
					case 0:
						System.out.println("수강관리 시스템을 종료합니다.");
						System.exit(1);
						break;
					case 1:
						update_Class();
						break;
					case 2:
						delete_Class();
						break;
					case 3:
						new GUI();
						break;
					default:	
						System.out.println("잘못 고르셨습니다");
				}
			}
		}
		else
		while(true)
		{
			System.out.println("메뉴를 선택해주세요");
			System.out.println("0.로그아웃 및 종료");
			System.out.println("1.강의 검색");
			System.out.println("2.신청 내역 확인하기");
			System.out.println("3.수강 신청");
			System.out.println("4.신청 과목 삭제하기");
			System.out.println("5.시간표 확인하기");
			System.out.println("6.신청 내역 되돌리기");
			
			int tmp = s.nextInt();
			
			switch(tmp)
			{
			case 0:
				System.out.println("수강신청 시스템을 종료합니다.");
				System.exit(0);
			case 1:
				System.out.println("1.수업번호 검색");
				System.out.println("2.학수번호 검색");
				System.out.println("3.교과목명 검색");
				System.out.println("4.교강사명검색");
				int select = s.nextInt();
				switch(select)
				{
					case 1:
						class_Info_Classid(student_id);
						break;
					case 2:
						class_Info_Courseid(student_id);
						break;
					case 3:
						class_Info_Classname(student_id);
						break;
					case 4:
						class_Info_Lecturername(student_id);
						break;
					default:
						System.out.println("잘못 선택하셨습니다.");
						
				}
				break;
			case 2:
				myClass(student_id);
				break;
			case 3:
				class_Enroll(student_id);
				enroll_Num += "O";
				break;
			case 4:
				class_Delete(student_id);
				break;
			case 5:
				schedule_Make(student_id);
				break;	
			case 6:
				enroll_Num = goBack(student_id,enroll_Num);
				break;
			}	
			
		}
	}
	
	public static String log_In() throws SQLException
	{
		String student_id;
		
		 Loop1 : while(true)
		{
		System.out.print("학번을 입력하세요 : ");
		String id = s.nextLine();
		sql = "SELECT * FROM student WHERE student_id = " + id;
		rs = stmt.executeQuery(sql);
			if(id.equals("9999"))
			{
				while(true)
				{
				System.out.print("비밀번호를 입력하세요 : ");
				String root_pw= s.nextLine();
					if(root_pw.equals("root"))
					{
						System.out.println("관리자 접속 승인");
						student_id = "9999";
						break Loop1;
					}
					else
					{
						j++;
						System.out.println("비밀번호가 틀렸습니다" + "(" + j + "/3)");
						if(j==3)
						{
							System.out.println("다시 확인해주세요");
							System.exit(0);
						}
					}
				}
			}
			if(rs.next())
			{
				while(true)
					{
					String name = rs.getString("name");
					System.out.print("비밀번호를 입력하세요 : ");
					String pw = s.nextLine();
					String str = rs.getString("password");
						if(str.equals(pw))
						{
							System.out.println("환영합니다 " + name +"님");
							student_id = rs.getString("student_id");
							break Loop1;
						}
						else
						{
							j++;
							System.out.println("비밀번호가 틀렸습니다" + "(" + j + "/3)");
								if(j==3)
								{
									System.out.println("다시 확인하시고 로그인 해주세요 !!");
									System.exit(0);
								}
							continue;
						}
					}
			}
			else
			{
				i++;
				System.out.println("학번이 존재하지 않습니다" + "("+ i + "/3)");
				if(i==3)
				{
					System.out.println("다시 확인하시고 로그인 해주세요 !!");
					System.exit(0);
				}
				continue;
			}
		}
		return student_id;
	}
	
	public static char day_Make(char day)
	{
		if(day == '1')
			return '월';
		if(day == '2')
			return '화';
		if(day == '3')
			return '수';
		if(day == '4')
			return '목';
		if(day == '5')
			return '금';
		if(day == '6')
			return '토';
		return 0;
	}
	
	public static int num_Make(char day)
	{
		if(day == '월')
			return 1;
		if(day == '화')
			return 2;
		if(day == '수')
			return 3;
		if(day == '목')
			return 4;
		if(day == '금')
			return 5;
		if(day == '토')
			return 6;
		return 0;
	}
                                        
	public static String[][] schedule_Blank(String[][] a) throws ParseException
	{
		
		String b="08:00";
		String c="08:30";
		String tmp[];
		
		Date bdate = new SimpleDateFormat("HH:mm").parse(b);
		Date edate = new SimpleDateFormat("HH:mm").parse(c);
		
		Calendar calendar = Calendar.getInstance();
		
		a[0][0] = "교시";
		a[0][1] = "월요일";
		a[0][2] = "화요일";
		a[0][3] = "수요일";
		a[0][4] = "목요일";
		a[0][5] = "금요일";
		
		for(i=1; i<33 ; i++)
		{
			a[i][0] = i + "교시 " + "(" + bdate + ")" + " ~ " + "(" + edate + ")";
			
			calendar.setTime(bdate);
			calendar.add(Calendar.MINUTE, 30);
			bdate = calendar.getTime();
			
			calendar.setTime(edate);
			calendar.add(Calendar.MINUTE, 30);
			edate = calendar.getTime();
			
			tmp = a[i][0].split(" ");
			tmp[4] = tmp[4].substring(0,5);
			tmp[11] = tmp[11].substring(0,5);
			
				a[i][0] = tmp[0]+" " + tmp[4] + " ~ " + tmp[11] ;
			
		}
		
		return a;
	}
	
	public static void class_Info_Classid(String student_id) throws SQLException
	{
		while(true)
		{
			
		System.out.print("수업 id를 입력하세요 : ");
		int class_id = s.nextInt();
		sql = "SELECT * FROM class WHERE class_id = " + class_id;
		rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				String class_name = rs.getString("name");
				String lecturer_id = rs.getString("lecturer_id");
				
				sql = "SELECT * FROM lecturer WHERE lecturer_id = " + lecturer_id;
				rs = stmt.executeQuery(sql);
				rs.next();
				String lecturer_name = rs.getString("name");
				System.out.print(class_name + " " + lecturer_name + " ");
				
				sql = "SELECT * FROM time WHERE class_id = " + class_id;
				rs = stmt.executeQuery(sql);
				rs.next();
				String begin = rs.getString("begin");
				if(begin.equals("NO"))
				{
					System.out.print("시간 미정");
				}
				else
				{
				String end = rs.getString("end");
				char day = begin.charAt(9);
				day = day_Make(day);
				String begin_time = begin.substring(11,16);	
				String end_time = end.substring(11,16);
				System.out.print(day + "요일 오후 " + begin_time + " ~ " + end_time + " " );
				}
				
				sql = "SELECT B.name FROM class C, room R, building B WHERE C.class_id = " + class_id + " AND C.room_id = R.room_id AND B.building_id = R.building_id";
				rs = stmt.executeQuery(sql);
				rs.next();
				String building_name = rs.getString("name");
				System.out.print(building_name + " "); 
				
				
				sql = "SELECT * FROM credits where student_id = " + student_id + " " + "and class_id = " + class_id;
				rs = stmt.executeQuery(sql);
				if(rs.next())
				{
					System.out.println("  재수강 O");
				}
				else
					System.out.println("  처음 수강");
					
				break;
				
			}
			else
			{
				System.out.println("수업 id를 다시 확인해주세요");
				continue;
			}
		}
	}

	public static void class_Info_Courseid(String student_id) throws SQLException
	{
		s.nextLine();
		Loop1 : while(true)
		{
		System.out.print("학수번호를 입력하세요 : ");
		String course_id = s.nextLine();
		sql = "SELECT * FROM class WHERE course_id = \"" + course_id+ "\"";
		rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				String class_name = rs.getString("name");
				String lecturer_id = rs.getString("lecturer_id");
				int class_id = rs.getInt("class_id");
				System.out.println(class_id);
				
				sql = "SELECT * FROM lecturer WHERE lecturer_id = " + lecturer_id;
				rs = stmt.executeQuery(sql);
				rs.next();
				String lecturer_name = rs.getString("name");
				System.out.print(class_name + " " + lecturer_name + " ");
				
				sql = "SELECT * FROM time,class WHERE class.course_id =  \"" + course_id + "\"" + " AND time.class_id = class.class_id";
				rs = stmt.executeQuery(sql);
				rs.next();
				String begin = rs.getString("begin");
				if(begin.equals("NO"))
				{
					System.out.print("시간 미정");
				}
				else
				{
				String end = rs.getString("end");
				char day = begin.charAt(9);
				day = day_Make(day);
				String begin_time = begin.substring(11,16);	
				String end_time = end.substring(11,16);
				System.out.print(day + "요일 오후 " + begin_time + " ~ " + end_time + " " );
				}
				
				sql = "SELECT B.name FROM class C, room R, building B WHERE C.course_id = \"" + course_id + "\"" + " AND C.room_id = R.room_id AND B.building_id = R.building_id";
				rs = stmt.executeQuery(sql);
				rs.next();
				String building_name = rs.getString("name");
				System.out.print(building_name + " "); 
				
				
				sql = "SELECT * FROM credits, class where credits.student_id = " + student_id + " " + "and class.course_id = \"" + course_id+ "\"" + "and credits.class_id = class.class_id";
				rs = stmt.executeQuery(sql);
				if(rs.next())
				{
					System.out.println("  재수강 O");
				}
				else
					System.out.println("  처음 수강");
					
				break Loop1;
				
			}
			else
			{
				System.out.println("학수번호를 다시 확인해주세요");
				continue;
			}
		}

	}
	
	public static void class_Info_Classname(String student_id) throws SQLException
	{
		s.nextLine();
		Loop1 : while(true)
		{
		int cnt=0;
		System.out.print("과목명을 입력하세요 (키워드 포함 검색) : ");
		String class_name = s.nextLine();
		sql = "SELECT * FROM class WHERE name LIKE \"%" + class_name + "%\"";
		rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				cnt++;
				String lecturer_id = rs.getString("lecturer_id");
				String class_fname = rs.getString("name");
				int class_id = rs.getInt("class_id");
				if(class_id >10000)
					continue;
				System.out.print(class_id + " ");
				sql = "SELECT * FROM lecturer WHERE lecturer_id = " + lecturer_id;
				rs_info = stmt.executeQuery(sql);
				rs_info.next();
				String lecturer_name = rs_info.getString("name");
				System.out.print(class_fname + " " + lecturer_name + " ");
				
				sql = "SELECT * FROM time,class WHERE class.class_id = " + class_id + " AND time.class_id = class.class_id ";
				rs_info = stmt.executeQuery(sql);
				rs_info.next();
				String begin = rs_info.getString("begin");
				if(begin.equals("NO"))
				{
					System.out.print("시간 미정 ");
				}
				else
				{
				String end = rs_info.getString("end");
				char day = begin.charAt(9);
				day = day_Make(day);
				String begin_time = begin.substring(11,16);	
				String end_time = end.substring(11,16);
				System.out.print(day + "요일 오후 " + begin_time + " ~ " + end_time + " " );
				}
				
				sql = "SELECT B.name FROM class C, room R, building B WHERE C.class_id = " + class_id + " AND C.room_id = R.room_id AND B.building_id = R.building_id";
				rs_info = stmt.executeQuery(sql);
				rs_info.next();
				String building_name = rs_info.getString("name");
				System.out.print(building_name + " "); 
				
				
				sql = "SELECT * FROM credits, class where credits.student_id = " + student_id + " " + "and class.class_id  = " + class_id + " and credits.class_id = class.class_id";
				rs_info = stmt.executeQuery(sql);
				if(rs_info.next())
				{
					System.out.println("  재수강 O");
				}
				else
					System.out.println("  처음 수강");
			}
			if(cnt ==0)
			{
				System.out.println("교과목명을 다시 확인해주세요");
				continue Loop1;
			}
			break Loop1;
		}

	}
	
	public static void class_Info_Lecturername(String student_id) throws SQLException
	{
		s.nextLine();
		Loop1 : while(true)
		{
		int cnt=0;
		System.out.print("교강사명을 입력하세요 (전방 일치 검색) : ");
		String lecturer_name = s.nextLine();
		sql = "SELECT * FROM class C, lecturer L WHERE L.name LIKE \"" + lecturer_name + "%\"" + " AND C.lecturer_id = L.lecturer_id";
		rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				cnt++;
				String lecturer_id = rs.getString("lecturer_id");
				String class_fname = rs.getString("name");
				int class_id = rs.getInt("class_id");
				if(class_id >10000)
					continue;
				System.out.print(class_id + " ");
				sql = "SELECT * FROM lecturer WHERE lecturer_id = " + lecturer_id;
				rs_info = stmt.executeQuery(sql);
				rs_info.next();
				lecturer_name = rs_info.getString("name");
				System.out.print(class_fname + " " + lecturer_name + " ");
				
				sql = "SELECT * FROM time,class WHERE class.class_id = " + class_id + " AND time.class_id = class.class_id ";
				rs_info = stmt.executeQuery(sql);
				rs_info.next();
				String begin = rs_info.getString("begin");
				if(begin.equals("NO"))
				{
					System.out.print("시간 미정 ");
				}
				else
				{
				String end = rs_info.getString("end");
				char day = begin.charAt(9);
				day = day_Make(day);
				String begin_time = begin.substring(11,16);	
				String end_time = end.substring(11,16);
				System.out.print(day + "요일 오후 " + begin_time + " ~ " + end_time + " " );
				}
				
				sql = "SELECT B.name FROM class C, room R, building B WHERE C.class_id = " + class_id + " AND C.room_id = R.room_id AND B.building_id = R.building_id";
				rs_info = stmt.executeQuery(sql);
				rs_info.next();
				String building_name = rs_info.getString("name");
				System.out.print(building_name + " "); 
				
				
				sql = "SELECT * FROM credits, class where credits.student_id = " + student_id + " " + "and class.class_id  = " + class_id + " and credits.class_id = class.class_id";
				rs_info = stmt.executeQuery(sql);
				if(rs_info.next())
				{
					System.out.println("  재수강 O");
				}
				else
					System.out.println("  처음 수강");
			}
			if(cnt ==0)
			{
				System.out.println("교과목명을 다시 확인해주세요");
				continue Loop1;
			}
			break Loop1;
		}

	}
	
	public static void myClass(String student_id) throws SQLException
	{
		
		String sql_info = "SELECT * FROM _" +student_id;
		rs_info = stmt_info.executeQuery(sql_info);
		while(rs_info.next())
		{
			System.out.printf("%10s %10s %30s %5s %30s %20s %20s\n",rs_info.getString("class_id"),rs_info.getString("course_id"),rs_info.getString("class_name"),rs_info.getString("retake"),rs_info.getString("lecturer_name"),rs_info.getString("time"),rs_info.getString("room") );
			
			/*System.out.printf("%20s ", rs_info.getString("class_id"));
			System.out.printf("%20s ", rs_info.getString("course_id"));
			System.out.printf("%20s ", rs_info.getString("class_name"));
			System.out.printf("%20s ", rs_info.getString("retake"));
			System.out.printf("%20s ", rs_info.getString("lecturer_name"));
			System.out.printf("%20s ", rs_info.getString("time"));
			System.out.printf("%20s \n", rs_info.getString("room"));*/
			
			/*System.out.print(rs_info.getString("class_id")+ " ");
			System.out.print(rs_info.getString("course_id") + " ");
			System.out.print(rs_info.getString("class_name") + " ");
			System.out.print(rs_info.getString("retake") + " ");
			System.out.print(rs_info.getString("lecturer_name") + " ");
			System.out.print(rs_info.getString("time") + " ");
			System.out.println(rs_info.getString("room"));*/
		}
		
	}
	
	public static String goBack(String student_id, String enroll_Num) throws SQLException
	{
		String new_Num ="";
		if(enroll_Num.equals(""))
			System.out.println("되돌릴 내역이 없습니다.");
		else
		{
			sql_info = "SELECT * FROM _" + student_id ;
			rs_info = stmt_info.executeQuery(sql_info);
			while(rs_info.next())
			{
				if(rs_info.isLast())
				{
					String class_rid = rs_info.getString("class_id");
					sql = "DELETE FROM _" + student_id +" WHERE class_id=" + class_rid;
					rs = stmt_info.executeQuery(sql);
				}
			}
			System.out.println("최근 신청한 과목이 삭제되었습니다.");
			new_Num = enroll_Num.substring(0,enroll_Num.length()-1);
		}
		return new_Num;
	}
	
	public static void class_Enroll(String student_id) throws SQLException
	{
		while(true)
		{
			System.out.print("신청할 수업 번호를 입력하세요 : ");
			int class_eid = s.nextInt();
			sql = "SELECT * FROM class WHERE class_id = " + class_eid;
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				String etime="";
				sql_info = "SELECT class_id FROM _" +student_id +  " WHERE class_id = " + class_eid ;
				rs_info = stmt_info.executeQuery(sql_info);
				if(rs_info.next())
				{
					System.out.println("이미 신청하신 수업입니다.");
				}
				else
				{
				String class_ename = rs.getString("name");
				System.out.print(class_ename + " ");
				
				String course_eid = rs.getString("course_id");
				System.out.print(course_eid + " ");
				
				sql = "SELECT L.name FROM lecturer L, class C WHERE class_id = " + class_eid + " AND L.lecturer_id = C.lecturer_id ";
				rs = stmt.executeQuery(sql);
				rs.next();
				String lecturer_ename = rs.getString("name");
				System.out.print(lecturer_ename + " ");
				
				sql = "SELECT * FROM time WHERE class_id = " + class_eid;
				rs = stmt.executeQuery(sql);
				rs.next();
				String begin = rs.getString("begin");
				if(begin.equals("NO"))
					System.out.print("시간 미정 ");
				else
				{
				String end = rs.getString("end");
				char day = begin.charAt(9); 
				day = day_Make(day);
				String begin_etime = begin.substring(11,16);	
				String end_etime = end.substring(11,16);
				etime = day + "요일 오후 " + begin_etime + " ~ " + end_etime + " " ;
				System.out.print(etime);
				}
				
				sql = "SELECT B.name, R.room_id FROM class C, room R, building B WHERE C.class_id = " + class_eid + " AND C.room_id = R.room_id AND B.building_id = R.building_id";
				rs = stmt.executeQuery(sql);
				rs.next();
				String building_ename = rs.getString("name");
				String room_eid = rs.getString("room_id");
				String eroom = building_ename + " " + room_eid + "호";
				System.out.print(eroom); 
				
				String retake = null;
				
				sql = "SELECT * FROM credits where student_id = " + student_id + " " + "and class_id = " + class_eid;
				rs = stmt.executeQuery(sql);
				if(rs.next())
				{
					retake = "Y";
					System.out.print("재수강 O\n");
				}
				else
				{
					retake = "N";
					System.out.print("처음 수강\n");
				}
				
				sql_info = "INSERT INTO _" +student_id+ " VALUES(" + class_eid + ",\"" + course_eid + "\",\"" + class_ename + "\",\"" + retake + "\",\"" + lecturer_ename + "\",\"" + etime + "\",\"" + eroom + "\");";
				
				rs_info = stmt_info.executeQuery(sql_info);
				
				System.out.println("신청 되셨습니다.");
				break;
				}
			}
			else
				System.out.println("개설되지 않은 수업 번호 입니다");
		}			
	}
	
	public static void class_Delete(String student_id) throws SQLException
	{
		while(true)
		{
			System.out.print("삭제할 수업 번호를 입력하세요 : ");
			int class_did = s.nextInt();
			sql_info = "SELECT class_id FROM _" + student_id + " WHERE class_id = " + class_did;
			rs_info = stmt_info.executeQuery(sql_info);
			if(rs_info.next())
			{
				sql_info = "DELETE FROM _" + student_id +" WHERE class_id = " + class_did;
				rs_info = stmt_info.executeQuery(sql_info);
				System.out.println("삭제되었습니다.");
				break;
			}
			else
			{
				System.out.println("신청 내역에 존재하지 않는 수업 번호입니다.");
			}
		}
	}
	
	public static void schedule_Make(String student_id) throws SQLException, ParseException
	{
		String[][] schedule = new String[33][6];
		for(i=0; i<33;i++)
			for(j=0;j<6;j++)
				schedule[i][j] = "";
	    schedule = schedule_Blank(schedule);
	    sql_info = "SELECT * FROM _" + student_id;
	    rs_info = stmt_info.executeQuery(sql_info);
	    while(rs_info.next())
	    {
	    	String time_tmp[];
	    	String name = rs_info.getString("class_name");
	    	String time_tmpN = rs_info.getString("time");
	    	if(time_tmpN.equals("NO"))
	    		break;
	    	else
	    	{
	    	time_tmp = time_tmpN.split(" ");
	    	String day = time_tmp[0];
	    	String time_tmp2 = time_tmp[2].split(":")[0] + time_tmp[2].split(":")[1];
	    	int time_num = Integer.parseInt(time_tmp2);
	    	time_tmp2 = time_tmp[4].split(":")[0] + time_tmp[4].split(":")[1];
	    	int time_num3 = Integer.parseInt(time_tmp2);
	    	time_num3 = (time_num3+1200)%2400;
	    	time_num = (time_num+1200)%2400;
	    	int k=1;
	    	while(true)
	    	{
	    		if(k>5)
	    			break;
	    		if(day.equals(schedule[0][k]))
	    			break;
	    		k++;
	    	}
	    	
	    	if(k==5)
	    	{
	    		if(time_num >1700)
	    			break;
	    	}
	    	
	    	
	    	j=1;
	    	while(true)
    		{
    			String bbb=schedule[j][0].split(" ")[3];
	    		bbb= bbb.split(":")[0] + bbb.split(":")[1];
	    		int time_num4 = Integer.parseInt(bbb);
	    		if(time_num3 == time_num4)
	    			break;
	    		j++;
    		}
	    	
	    	for(i=1; i<33; i++)
	    	{
	    		if(k>5)
	    			break;
	    		String aaa= schedule[i][0].split(" ")[1];
	    		aaa= aaa.split(":")[0] + aaa.split(":")[1];
	    		int time_num2 = Integer.parseInt(aaa);
	   
	    		if(time_num == time_num2)
	    			while(i<=j)
	    			{
	    				schedule[i][k] = schedule[i][k]+ " " + name;
	    				i++;
	    			}
	    	}
	    	}
	    }
		for(i=0 ; i<33 ; i++)
		{
			for(j=0; j<6 ; j++)
			{
					if(i==0)
					{
						System.out.printf("%20s", schedule[i][j]);	
						System.out.printf("|");
					}
					else
					{
						System.out.printf("%20s", schedule[i][j]);
						
					}
			}
			System.out.println();
		}
		
	
	}

	public static void update_Class() throws SQLException
	{
		while(true)
		{	
			int class_no;
			String course_id;
			
			System.out.print("개설하실 수업의 번호를 입력하세요(class_id) : ");
			int class_id = s.nextInt();
			sql = "SELECT * FROM class WHERE class_id = " + class_id;
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				System.out.println("존재하는 수업 번호입니다. 개설할 수 없습니다.");
				continue;
			}
			s.nextLine();
			while(true)
			{
			System.out.print("개설하실 학수 번호를 입력하세요  : ");
			course_id = s.nextLine();
			
			sql = "SELECT * FROM class WHERE course_id = \"" + course_id + "\"";
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				System.out.println("존재하는 학수 번호입니다. 개설할 수 없습니다.");
				continue;
			}
			break;
			}
			
			while(true)
			{
			System.out.print("개설하실 수업의 번호2를 입력하세요(class_no) : ");
			class_no = s.nextInt();
			
			sql = "SELECT * FROM class WHERE class_no = " + class_no;
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				System.out.println("존재하는 수업 번호2입니다. 개설할 수 없습니다.");
				continue;
			}
			break;
			}
			s.nextLine();
			System.out.print("개설하실 수업의 이름을 입력하세요 : ");
			String class_name = s.nextLine();
			
			System.out.print("전공번호(1~89), 신청학년(1~4), 학점을 입력하세요(1~4) : ");
			int major_id = s.nextInt();
			int year = s.nextInt();
			int credit = s.nextInt();
			int lecturer_id;
			while(true)
			{
			System.out.print("교강사 학번을 입력하세요 : ");
			lecturer_id = s.nextInt();
			sql = "SELECT * FROM class WHERE lecturer_id = " + lecturer_id;
			rs = stmt.executeQuery(sql);
			if(!rs.next())
			{
				System.out.println("존재하지 않는 교강사 학번입니다.");
				continue;
			}
			break;
			}
			
			System.out.print("수강인원을 입력하세요 : ");
			int person_max = s.nextInt();
			
			System.out.print("개설 년도를 입력하세요 : ");
			int opened = s.nextInt();
			
			System.out.print("개설 학기를 입력하세요 : ");
			int period = s.nextInt();
			
			s.nextLine();
			System.out.print("개설 시간을 입력하세요(예 : 월요일 08:00 ~ 10:30) : ");
			String time = s.nextLine();
			int day = num_Make(time.charAt(0));
			String begin = time.substring(4, 8);
			String end = time.substring(12, 16);
			
			
			System.out.print("강의실 호수를 입력하세요(1~199) : ");
			int room_id = s.nextInt();
			
			sql = "INSERT INTO class VALUES (\"" + class_id + "\",\"" + class_no + "\",\"" + course_id + "\",\"" + class_name + "\",\"" + major_id + "\",\"" +year + "\",\"" + credit + "\",\"" +lecturer_id + "\",\"" +person_max + "\",\"" +opened+ "\",\"" +room_id+ "\")"; 
			rs = stmt.executeQuery(sql);
			sql = "INSERT INTO course VALUES (\"" + course_id + "\",\"" +class_name+ "\",\"" +credit + "\")";
			rs = stmt.executeQuery(sql);
			sql = "select time_id from time";
			rs = stmt.executeQuery(sql);
			int time_id=0;
			while(rs.next())
			{
				if(rs.isLast())
					time_id = rs.getInt("time_id");
			}
			time_id++;
			sql = "INSERT INTO time VALUES (\"" + time_id+ "\",\"" +class_id+ "\",\"" +period+ "\",\"" + "1900-01-0" + day + "T" + begin + ":00.000Z" + "\",\"" + "1900-01-0" + day + "T" + end + ":00.000Z\")";
			rs = stmt.executeQuery(sql);
			System.out.println("개설되었습니다.");
			break;
		}
	}
	
	public static void delete_Class() throws SQLException
	{
		while(true)
		{
		System.out.print("삭제할 수업 번호를 입력하세요 : ");
		int class_did = s.nextInt();
		s.nextLine();
		sql = "SELECT * FROM class WHERE class_id =" + class_did;
		rs = stmt.executeQuery(sql);
		if(rs.next())
		{
			sql = "DELETE FROM class WHERE class_id = " + class_did;
			rs = stmt.executeQuery(sql);
			sql = "DELETE FROM time WHERE class_id = " + class_did;
			rs = stmt.executeQuery(sql);		
		}
		else
		{
			System.out.println("없는 수업 번호입니다.");
			continue;
		}
		break;
		}
	}
	
	@SuppressWarnings("serial")
	static class GUI extends JFrame
	{
		GUI() throws SQLException {
			setTitle("학부별 인원수 통계");  //windows 제목 출력, 생략가능
			this.setLayout(new BorderLayout());
			
			String[] major={"건설환경공학과", "소프트웨어전공", "컴퓨터전공", "전기공학전공", "건축공학부", "화학공학전공", "생명공학전공", "유기나노공학과", "에너지공학과"};
						
			JComboBox<String> combo = new JComboBox<String>(major);
			//String major_name = null;
			add(combo, BorderLayout.BEFORE_FIRST_LINE);
			setSize(2000,500);
			setVisible(true);	
			
			combo.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String major_name;
					major_name= combo.getSelectedItem().toString();
					
					String sql = "SELECT * FROM major WHERE name = \"" + major_name + "\"";
					
					
					
					try {
					
						String[][] info = new String[4][20];
						
						info[0][0] = "2011";
						info[1][0] = "2012";
						info[2][0] = "2013";
						info[3][0] = "2014";
						
						String[] classify = new String[20];
						classify[0] = "연도 〵교과목";
						
						int a=0,b=0,c=0,d=0,tmp=0,i=1,j=1;
						int major_num;
						
						rs = stmt.executeQuery(sql);
						rs.next();
						major_num = rs.getInt("major_id");
						sql = "SELECT * FROM class WHERE major_id = " + major_num;
						rs_info = stmt.executeQuery(sql);
						
						for(j=1;j<classify.length;j++)
							classify[j] = "";
						
						loop2 : while(rs_info.next())
						{
							
							String class_name = rs_info.getString("name");
							
							for(j=1; !classify[j].equals("") ; j++)
								if(classify[j].equals(class_name))
									continue loop2;
							
							classify[j] = class_name;
							sql = "SELECT opened FROM class where name = \"" + class_name + "\"";
							rs = stmt.executeQuery(sql);
							
							a=0;b=0;c=0;d=0;
							
							while(rs.next())
							{
							tmp = rs.getInt("opened");
							
								switch(tmp)
								{
								case 2011:
									a++;
									break;
								case 2012:
									b++;
									break;
								case 2013:
									c++;
									break;
								case 2014:
									d++;
									break;
								}
							}
							info[0][i] = String.valueOf(a);
							info[1][i] = String.valueOf(b);
							info[2][i] = String.valueOf(c);
							info[3][i] = String.valueOf(d);
							i++;
						}
						
						DefaultTableModel model = new DefaultTableModel(info,classify);
						
						JTable year = new JTable(model);
						JScrollPane sc = new JScrollPane(year);
						
						//add(combo, BorderLayout.BEFORE_FIRST_LINE);
						
						add(sc);
						//super("스윙");
						setSize(2000,500);
						setVisible(true);	
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
		}
	}
	
}