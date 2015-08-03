

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class CalendarGUI extends JFrame implements MouseListener{
	private JPanel panel;
	private JLabel[] title;
	private JLabel date_above;
	private JTextField[] ShowDay;
	private Calendar calendar;
	private Date today;
	private JButton up, down;
	private String Week[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	
	private JFrame father;
	private ArrayList<PIMEntity> List = new ArrayList<PIMEntity>();
	private ArrayList<PIMEntity> hasDate=new ArrayList<PIMEntity>();
	
	public CalendarGUI(ArrayList<PIMTodo> l,ArrayList<PIMAppointment> a){
		
		//this.List = l;
		for(PIMTodo x : l)
		{
			x.setDate();
			hasDate.add(x);
		}
		for(PIMAppointment x:a)
		{
			x.setDate();
			hasDate.add(x);
		}
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		today = calendar.getTime();
		
		
		panel = new JPanel();
		JPanel center = new JPanel();
		JPanel north = new JPanel();
		center.setLayout(new GridLayout(7,7));
		up = new JButton("Last Month");
		down = new JButton("Next Month");
		up.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0){
				int month = calendar.get(Calendar.MONTH);
				month --;
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				if(month<0){
					calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)-1);
					calendar.set(Calendar.MONTH, 11);
				}
				else{
					calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
				}
				setCalendar();
			}
		});
		
		down.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0){
				int month = calendar.get(Calendar.MONTH);
				month ++;
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				if(month>11){
					calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+1);
					calendar.set(Calendar.MONTH, 0);
				}
				else{
					calendar.set(Calendar.MONTH, month);
				}
				setCalendar();
			}
		});
		north.setLayout(new FlowLayout());
		north.add(up);
		north.add(down);
		
		title = new JLabel[7];
		ShowDay = new JTextField[42];
		
		for(int i=0;i<7;i++){
			title[i] = new JLabel(Week[i]);
			title[i].setBorder(BorderFactory.createRaisedBevelBorder());
			center.add(title[i]);
		}
		for(int i=0;i<42;i++){
			ShowDay[i] = new JTextField();
			ShowDay[i].addMouseListener(this);
			ShowDay[i].setEditable(false);
			center.add(ShowDay[i]);
		}
		date_above = new JLabel("Date:"+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR));
		north.add(date_above);
		panel.setLayout(new BorderLayout());
		panel.add(north,BorderLayout.NORTH);
		panel.add(center,BorderLayout.CENTER);
		//panel.add(new JLabel("Author:GQ"),BorderLayout.SOUTH);
		
		Container cc = getContentPane();
		cc.add(panel);
		setCalendar();
		setVisible(true);
		setBounds(380,250,524,285);
	}
	
	public void setCalendar(){
		date_above.setText("Date:"+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR));
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		int month = calendar.get(Calendar.MONTH)+1;
		int year = calendar.get(Calendar.YEAR);
		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12)
			arrangeNumber(dayOfWeek, 31);
		else if(month==4||month==6||month==9||month==11)
			arrangeNumber(dayOfWeek,30);
		else if(month==2){
			if((year%4==0&&year%100!=0)||(year%400==0))
				arrangeNumber(dayOfWeek,29);
			else
				arrangeNumber(dayOfWeek,28);
		}
		
		
	}
	public void arrangeNumber(int dayOfWeek, int daysOfMonth){
		
		Color origin = getBackground();
		Calendar tmp_cal = calendar;
		
		for(int i=dayOfWeek,n=1;i<dayOfWeek+daysOfMonth;i++){
			
			
			ShowDay[i].setText(""+n);
			calendar.set(Calendar.DAY_OF_MONTH, n);
			
			//PIMCollection collection = new PIMCollection();
			//System.out.print(calendar.getTime());
			ArrayList<PIMEntity> entities = new ArrayList<>();// = collection.getItemsForDate(calendar.getTime());
			for(PIMEntity x : hasDate)
			{
				
				if(x.date==null)	continue;
				
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String dd=year+"/"+month+"/"+day;
				
				if(x.date.equals(dd))
					entities.add(x);
			}
			
			if(entities.size()!=0)
				ShowDay[i].setBackground(Color.RED);
			else
				ShowDay[i].setBackground(origin);
			
			if(today.equals(tmp_cal.getTime())){
				ShowDay[i].setForeground(Color.BLUE);
				ShowDay[i].setFont(new Font("TimesRoman",Font.BOLD,20));
			}
			else{
				ShowDay[i].setFont(new Font("TimesRoman",Font.BOLD,12));
				ShowDay[i].setForeground(Color.black);
			}
			/*if(i%7==6){
				ShowDay[i].setForeground(Color.blue);
			}
			if(i%7==0){
				ShowDay[i].setForeground(Color.red);
			}*/
			n++;
		}
		for(int i=0;i<dayOfWeek;i++){
			ShowDay[i].setText("");
		}
		for(int i=dayOfWeek+daysOfMonth;i<42;i++){
			ShowDay[i].setText("");
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Component tmp = e.getComponent();
		if(tmp.getBackground().equals(Color.red)){
			JTextField t = (JTextField)tmp;
			calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(t.getText()));
			
			//PIMCollection collection = new PIMCollection();
			ArrayList<PIMEntity> entities = new ArrayList<>();// = collection.getItemsForDate(calendar.getTime());
			for(PIMEntity x : hasDate)
			{
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String dd=year+"/"+month+"/"+day;
				if(x.date.equals(dd))
					entities.add(x);
			}
			ShowEventOnDate(entities);
		}
			
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void ShowEventOnDate(ArrayList<PIMEntity> l){//here is the detail
		
		  String result = "";
		  for(PIMEntity p:l)
			  result += p.toString() + "\n";
			
		  final JDialog d1 = new JDialog();
		  JPanel panel = new JPanel();
		  JButton confirm = new JButton("Confirm");
		  JTextArea text = new JTextArea();
		  
		  d1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		  
		  d1.setLocation(445, 260);
		  d1.setSize(400, 300);
		  d1.getContentPane().setLayout(null);
		  d1.getContentPane().add(panel);   
		  d1.setVisible(true);
		  
		  panel.setLayout(null);
		  panel.setLocation(0, 0);
		  panel.setSize(400, 300);
		  confirm.setLocation(90, 200);
		  confirm.setSize(80, 30);
		  confirm.setBounds(145, 200, 80, 30);
		  text.setBounds(5, 25, 375, 160);
		  text.setEditable(false);
		  text.setText(result);
		  
		  
		  confirm.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent arg0){
				  d1.dispose();
			  }
		});
		  panel.add(confirm);
		  panel.add(text);
		
	}

}
