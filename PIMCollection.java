import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Scanner;

class MyObjectOutputStream extends ObjectOutputStream //////////////////////caution
{ 
	MyObjectOutputStream() throws IOException 
	{  
       super(); 
	}
	public MyObjectOutputStream(OutputStream out) throws IOException 
	{
		super(out);
	} 
}
abstract class PIMEntity {
    public  String type;
    public  String text;
    public  String description;
    public  String date;
    public  String Priority; // every kind of item has a priority
    
    // default constructor sets priority to "normal"
    PIMEntity() {
        
    }
    
    // priority can be established via this constructor.
    
    // accessor method for getting the priority string
    
    // method that changes the priority string
    
    
    // Each PIMEntity needs to be able to set all state information
    // (fields) from a single text string.
    abstract public void fromString(String s);
    
    // This is actually already defined by the super class
    // Object, but redefined here as abstract to make sure
    // that derived classes actually implement it
    abstract public String toString();
}
interface DATE
    {
        String date="2014/3/1";
    }
class PIMTodo extends PIMEntity implements DATE,Serializable
{
	public String type;
	public String text;
    public String date;
    public Date d;
    public String Priority;
    PIMTodo(String Priority,String date,String text)
    {
        this.type="todo";
        this.Priority=Priority;
        this.date=date;
        super.date = date;
        this.text=text;
        //System.out.println("this priority: "+this.Priority);
        //System.out.println("priority: "+Priority);
        String[] datesplit=date.split("/");
        if(datesplit.length==3)
        {
        		int year = Integer.parseInt(datesplit[0].trim());//delete the space
        	        int month = Integer.parseInt(datesplit[1].trim());
        	        int day = Integer.parseInt(datesplit[2].trim());
        	        Calendar c = Calendar.getInstance();
        	        c.set(year, month-1, day);//set the time for calendar instance
        	        d = c.getTime();
        	}
       

    }
    public void fromString(String s)
    {
    }
    public String toString()
    {
        return "TODO"+" "+this.Priority +" "+this.date+" "+this.text;
    }
    public void setDate(){
    	super.date = date;
    }
}
class PIMNote extends PIMEntity implements Serializable
{
	public String type;
    public String text;
    public String date;
    public String Priority;
    
    PIMNote(String Priority,String text)
    {
        this.type="note";
        this.Priority=Priority;
        this.text=text;
    }
    public void fromString(String s)
    {
    }
    public String toString()
    {
        return "NOTE"+" "+this.Priority+" "+this.text;
    }
    
}
class PIMAppointment extends PIMEntity implements DATE,Serializable
{
	public String type="appointment";
    String description;
    String date;
    Date d;
    PIMAppointment(String Priority,String date,String description)
    {
        this.type="appointment";
        this.Priority=Priority;
        this.date=date;
        super.date=date;
        this.description=description;
        String[] datesplit=date.split("/");
        if(datesplit.length==3)
        {
        		int year = Integer.parseInt(datesplit[0].trim());
        	        int month = Integer.parseInt(datesplit[1].trim());
        	        int day = Integer.parseInt(datesplit[2].trim());
        	        Calendar c = Calendar.getInstance();
        	        c.set(year, month-1, day);
        	        d = c.getTime();
        	}
    }
    public void fromString(String s)
    {
    }
    public String toString()
    {
        return "APPOINTMENT"+" "+this.Priority+" "+this.date+" "+this.description;
    }
    public void setDate(){
    	super.date = date;
    }
    
}
class PIMContact extends PIMEntity implements Serializable
{
    /*priority (a string), and strings for each of the following: first name, last name, email address.*/
	public String type="contact";
	String Priority;
	String firstName;
    String lastName;
    String emailAddress;
    public String date="0/0/0";
    PIMContact(String Priority,String firstName,String lastName,String emailAddress)
    {
        this.type="contact";
        this.Priority=Priority;
        this.firstName=firstName;
        this.lastName=lastName;
        this.emailAddress=emailAddress;
    }
    public void fromString(String s)
    {
    }
    public String toString()
    {
        return "CONTACT"+" "+this.Priority+" "+this.firstName+" "+this.lastName+" "+this.emailAddress;
    }
}
public class PIMCollection extends List//ArrayList
{
	//static PIMEntity data[]=new PIMEntity[100];//All these four kind of items are kept in arrays before wirtten into file
	public static String user=null;//user's name for the directory
	public static int todoTotal=0;
	public static int noteTotal=0;
	public static int appointmentTotal=0;
	public static int contactTotal=0;
	public static int allTotal=todoTotal+noteTotal+contactTotal+appointmentTotal;
	public static int flagTotal=todoTotal*1000+noteTotal*100+appointmentTotal*10+contactTotal;
	
	
	public static int todoInFile=0;
	public static int noteInFile=0;
	public static int appointmentInFile=0;
	public static int contactInFile=0;
	public static int flagFile=todoInFile*1000+noteInFile*100+appointmentInFile*10+contactInFile;
	
	public static PIMTodo tempTodo[]=new PIMTodo[100];
	public static PIMNote tempNote[]=new PIMNote[100];
	public static PIMAppointment tempAppointment[]=new PIMAppointment[100];
	public static PIMContact tempContact[]=new PIMContact[100];
	
	public static Scanner s=new Scanner(System.in);
	static int total=0;
	static int tempTotal=0;
	public static ArrayList<PIMNote> allNotes=new ArrayList();
	public static ArrayList<PIMTodo> allTodos=new ArrayList();
	public static ArrayList<PIMAppointment> allAppointments=new ArrayList();
	public static ArrayList<PIMContact> allContacts=new ArrayList();
	public static ArrayList<PIMEntity> allItems=new ArrayList();
	
        PIMCollection(String user)
	{
		this.user=user;
	}
	static ArrayList getNotes()
	{
		return allNotes;
		
	}
	static ArrayList getTodos()
	{
		return allTodos;
		
	}
	
	static ArrayList getAppointments()
	{
		return allAppointments;
		
	}
	
	static ArrayList getContacts()
	{
		return allContacts;
		
	}
	
	static ArrayList getItemsForDate(String a)
	{
		ArrayList<PIMEntity> allDates=new ArrayList();
		//System.out.println(tempTodo[0].date);//afdfasfasfasafsafafafadfafaf problem is here
		for(int i=0;i<todoTotal;i++)
			if(tempTodo[i].date.equals(a))//test here
			{
				//System.out.println(tempTodo[0].date);
				allDates.add(tempTodo[i]);
				//System.out.println("bbinggo");//adsfafasfafafdaafsdfafa
			}
		for(int i=0;i<noteTotal;i++)
			if(tempNote[i].date.equals(a))
				allDates.add(tempNote[i]);
		for(int i=0;i<appointmentTotal;i++)
			if(tempAppointment[i].date.equals(a))
				allDates.add(tempAppointment[i]);
		for(int i=0;i<contactTotal;i++)
			if(tempContact[i].date.equals(a))
				allDates.add(tempContact[i]);
		//System.out.println("this is a test");
		return allDates;
	}
	/*static void lists()
     {
     for(int i=0;i<todoTotal;i++)
     System.out.println(tempTodo[i].toString());
     for(int i=0;i<noteTotal;i++)
     System.out.println(tempNote[i].toString());
     for(int i=0;i<contactTotal;i++)
     System.out.println(tempContact[i].toString());
     for(int i=0;i<appointmentTotal;i++)
     System.out.println(tempAppointment[i].toString());
     System.out.println("*********************The end of all the items***********************");
     
     }*/
	static void init()
	{
		String pathTodo="/Users/lee/Desktop/"+user+"todo.txt";
		String pathNote="/Users/lee/Desktop/"+user+"note.txt";
		String pathAppointment="/Users/lee/Desktop/"+user+"appointment.txt";
		String pathContact="/Users/lee/Desktop/"+user+"contact.txt";
		
		String userdir="/Users/lee/Desktop/"+user;//attention!!!!!
		//String pathAll="/Users/lee/Desktop/all.txt";
		
		
		File Todo=new File(pathTodo);
		File Note=new File(pathNote);
		File Appointment=new File(pathAppointment);
		File Contact=new File(pathContact);
		File User=new File(userdir);
		//File All=new File(pathAll);
		//decide whether the directory exist
		if(!User.exists())
			User.mkdir();
		
		
		if(!Todo.exists())
			try {
				Todo.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(!Note.exists())
			try {
				Note.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(!Appointment.exists())
			try {
				Appointment.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(!Contact.exists())
			try {
				Contact.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		/*if(!All.exists())
			try {
				All.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		
			
	}
	static String[] lists()//brand new one which has selections of getNotes, getTodos etc.
	{
                                String[] result=new String[4];
				Iterator it=getNotes().iterator();
                                String detailnote=new String();
				while(it.hasNext())
                                    detailnote+=it.next().toString()+"\n";
                                //System.out.println(it.next().toString());
				//System.out.println("************the end of***************");
				result[0]=detailnote;
                                
				it=getTodos().iterator();
				String detailtodo=new String();
                                while(it.hasNext())
                                    detailtodo+=it.next().toString()+"\n";
					//System.out.println(it.next().toString());
				
				//System.out.println("************the end***************");
				result[1]=detailtodo;
                                
				it=getAppointments().iterator();
                                String detailappointment=new String();
				while(it.hasNext())
                                    detailappointment+=it.next().toString()+"\n";
					//System.out.println(it.next().toString());
				//System.out.println("************the end***************");
				result[2]=detailappointment;
                                
				it=getContacts().iterator();
                                String detailcontact=new String();
				while(it.hasNext())
                                       detailcontact+=it.next().toString()+"\n";
//System.out.println(it.next().toString());
				//System.out.println("************the end***************");
				result[3]=detailcontact;
                                
				//System.out.println("Type in the date in format of yy/mm/dd");
                                 return result;
	}
	
	
	/*static void create()
	{
		System.out.println("Enter an item type ( todo, note, contact or appointment )");
		String item=s.next();
		switch(item)
		{
			case "todo":
				createTodo();
				break;
			case "note":
				createNote();
				break;
			case "contact":
				createContact();
				break;
			case "appointment":
				createAppointment();
				break;
		}
		
	}*/
	/*static void save() throws FileNotFoundException//当用户键入 save 时，将临时数组中的数据写入文件
	{
		int i=0;
		int j=0;
		System.out.println("Save succeed!");
		
        FileOutputStream out=new FileOutputStream("/Users/lee/Documents/java/test/test.txt");
        PrintStream p=new PrintStream(out);
        for(i=0;i<todoTotal;i++)
        {
            //if(temp[i].date==null) System.out.println("1");
            //System.out.println(temp[i].toString());
            p.println("todo");
            p.println(tempTodo[i].Priority);
            p.println(tempTodo[i].date);
            p.println(tempTodo[i].text);
            
        }
        for(i=0;i<noteTotal;i++)
        {
            //if(temp[i].date==null) System.out.println("1");
            //System.out.println(temp[i].toString());
            p.println("note");
            p.println(tempNote[i].Priority);
            p.println(tempNote[i].text);
            
        }for(i=0;i<contactTotal;i++)
        {
            //if(temp[i].date==null) System.out.println("1");
            //System.out.println(temp[i].toString());
            p.println("contact");
            p.println(tempContact[i].Priority);
            p.println(tempContact[i].firstName);
            p.println(tempContact[i].lastName);
            p.println(tempContact[i].emailAddress);
            
        }for(i=0;i<appointmentTotal;i++)
        {
            //if(temp[i].date==null) System.out.println("1");
            //System.out.println(temp[i].toString());
            p.println("appointment");
            p.println(tempAppointment[i].Priority);
            p.println(tempAppointment[i].date);
            p.println(tempAppointment[i].description);
            
        }
        
	}*/
	static void save() throws FileNotFoundException//
	{//already in the array and in the file
		String pathTodo="/Users/lee/Desktop/"+user+"todo.txt";//check hereererererererererererererere
		String pathNote="/Users/lee/Desktop/"+user+"note.txt";
		String pathAppointment="/Users/lee/Desktop/"+user+"appointment.txt";
		String pathContact="/Users/lee/Desktop/"+user+"contact.txt";
		String pathAll="/Users/lee/Desktop/"+user+"all.txt";
		ObjectOutputStream out=null;
	////////////////////////////////////////////////////////////////////////////////////////delete before save
		for(PIMTodo x: allTodos)
		{
			allItems.add(x);
		}
		for(PIMNote x:allNotes)
		{
			allItems.add(x);
		}
		for(PIMAppointment x:allAppointments)
		{
			allItems.add(x);
		}
		for(PIMContact x:allContacts)
		{
			allItems.add(x);
		}
		File Todo=new File(pathTodo);
		File Note=new File(pathNote);
		File Appointment=new File(pathAppointment);
		File Contact=new File(pathContact);
		File All=new File(pathAll);
		
		if(Todo.exists())
			Todo.delete();
		if(Note.delete())
			Note.delete();
		if(Appointment.exists())
			Appointment.delete();
		if(Contact.exists())
			Contact.delete();
		if(All.exists())
			All.delete();
		
		try {
			File file=new File(pathTodo);
			FileOutputStream fos=new FileOutputStream(file,true);//delete true
			if(file.length()<1)
			{
				out=new ObjectOutputStream(fos);
			}
			else
			{
				out=new MyObjectOutputStream(fos);
			}
			for(int i=0;i<todoTotal;i++)
			{
				out.writeObject(tempTodo[i]);
			}
			
			///save how many todos!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			FileOutputStream os=new FileOutputStream("/Users/lee/Desktop/"+user+"todoNum.txt");
			PrintStream p=new PrintStream(os);
			//System.out.println("todoTotal is: "+todoTotal);
			p.println(todoTotal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			File file=new File(pathNote);
			FileOutputStream fos=new FileOutputStream(file,true);
			if(file.length()<1)
			{
				out=new ObjectOutputStream(fos);
			}
			else
			{
				out=new MyObjectOutputStream(fos);
			}
			for(int i=0;i<noteTotal;i++)
			{
				out.writeObject(tempNote[i]);
			}
			FileOutputStream os=new FileOutputStream("/Users/lee/Desktop/"+user+"noteNum.txt");
			PrintStream p=new PrintStream(os);
			p.println(noteTotal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			File file=new File(pathAppointment);
			FileOutputStream fos=new FileOutputStream(file,true);
			if(file.length()<1)
			{
				out=new ObjectOutputStream(fos);
			}
			else
			{
				out=new MyObjectOutputStream(fos);
			}
			for(int i=0;i<appointmentTotal;i++)
			{
				out.writeObject(tempAppointment[i]);
			}
			
			FileOutputStream os=new FileOutputStream("/Users/lee/Desktop/"+user+"appointmentNum.txt");
			PrintStream p=new PrintStream(os);
			p.println(appointmentTotal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			File file=new File(pathContact);
			FileOutputStream fos=new FileOutputStream(file,true);
			if(file.length()<1)
			{
				out=new ObjectOutputStream(fos);
			}
			else
			{
				out=new MyObjectOutputStream(fos);
			}
			for(int i=0;i<contactTotal;i++)
			{
				out.writeObject(tempContact[i]);
			}
			
			FileOutputStream os=new FileOutputStream("/Users/lee/Desktop/"+user+"contactNum.txt");
			PrintStream p=new PrintStream(os);
			p.println(contactTotal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			File file=new File(pathAll);
			FileOutputStream fos=new FileOutputStream(file,true);
			if(file.length()<1)
			{
				out=new ObjectOutputStream(fos);
			}
			else
			{
				out=new MyObjectOutputStream(fos);
			}
			for(PIMEntity x: allItems)////////////careful
			{
				out.writeObject(x);
			}
			
			FileOutputStream os=new FileOutputStream("/Users/lee/Desktop/"+user+"allNum.txt");
			PrintStream p=new PrintStream(os);
			p.println(allTotal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	static void load() throws IOException
	{
		String pathTodo="/Users/lee/Desktop/"+user+"todo.txt";
		String pathNote="/Users/lee/Desktop/"+user+"note.txt";
		String pathAppointment="/Users/lee/Desktop/"+user+"appointment.txt";
		String pathContact="/Users/lee/Desktop/"+user+"contact.txt";
		String pathAll="/Users/lee/Desktop";
       
        
        
        ObjectInputStream in=null;
        try {
        		in=new ObjectInputStream(new FileInputStream(pathTodo));
        		//System.out.println(in.readObject().getClass());
        		todoTotal=0;
        		//read the number of todo in file
        		BufferedReader br=new BufferedReader(new FileReader("/Users/lee/Desktop/"+user+"todoNum.txt"));
        		todoInFile=Integer.parseInt(br.readLine());
        		//System.out.println("todoInFile is: "+todoInFile);
        		while(true)
        		{
        			tempTodo[todoTotal]=(PIMTodo) in.readObject();
        			//System.out.println(tempTodo[todoTotal]);
        			//System.out.println("TODO"+" "+tempTodo[todoTotal].Priority +" "+tempTodo[todoTotal].date+" "+tempTodo[todoTotal].text);
        			//PIMCollection.allTodos=new ArrayList();
        			PIMCollection.allTodos.add(tempTodo[todoTotal]);
        			todoTotal++;
        		}
        	}
        	catch(EOFException e)
        	{
        		
        	}
        	catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
    		in=new ObjectInputStream(new FileInputStream(pathNote));
    		noteTotal=0;
    		BufferedReader br=new BufferedReader(new FileReader("/Users/lee/Desktop/"+user+"noteNum.txt"));
    		noteInFile=Integer.parseInt(br.readLine());
    		
    		while(true)
    		{
    			tempNote[noteTotal]=(PIMNote) in.readObject();
    			PIMCollection.allNotes.add(tempNote[noteTotal]);
    			noteTotal++;
    		}
    	}
    	catch(EOFException e)
    	{
    		
    	}
    	catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
        try {
    		in=new ObjectInputStream(new FileInputStream(pathAppointment));
    		appointmentTotal=0;
    		BufferedReader br=new BufferedReader(new FileReader("/Users/lee/Desktop/"+user+"appointmentNum.txt"));
    		noteInFile=Integer.parseInt(br.readLine());
    		
    		while(true)
    		{
    			tempAppointment[appointmentTotal]=(PIMAppointment) in.readObject();
    			//System.out.println(tempTodo[appointmentTotal]);
    			PIMCollection.allAppointments.add(tempAppointment[appointmentTotal]);
    			appointmentTotal++;
    		}
    	}
    	catch(EOFException e)
    	{
    		
    	}
    	catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
        try {
    		in=new ObjectInputStream(new FileInputStream(pathContact));
    		contactTotal=0;
    		BufferedReader br=new BufferedReader(new FileReader("/Users/lee/Desktop/"+user+"contactNum.txt"));
    		contactInFile=Integer.parseInt(br.readLine());
    		
    		while(true)
    		{
    			tempContact[contactTotal]=(PIMContact) in.readObject();
    			//System.out.println(tempContact[contactTotal]);
    			PIMCollection.allContacts.add(tempContact[contactTotal]);
    			contactTotal++;
    		}
    	}
    	catch(EOFException e)
    	{
    		
    	}
    	catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
        
        //System.out.println("************the end***************");
	}
	/*
	static void load() throws IOException
	{
        String a,b,c,d,e;
        File file=new File("/Users/lee/Documents/java/test/test.txt");
        //System.out.println("afdasfsdaafaf");
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br=new BufferedReader(new FileReader(file));
        String temp=null;
        StringBuffer sb=new StringBuffer();
        temp=br.readLine();
        while(temp!=null){
            switch(temp)
            {
                case "todo":
                    a=br.readLine();
                    b=br.readLine();
                    c=br.readLine();
                    System.out.println("TODO "+a+" "+b+" "+c);
                    PIMCollection.tempTodo[todoTotal]=new PIMTodo(a,b,c);//modified
                    PIMCollection.allTodos.add(tempTodo[todoTotal]);
                    todoTotal++;
                    
                    
                    temp=br.readLine();
                    break;
                case "note":
                    a=br.readLine();
                    b=br.readLine();
                    System.out.println("NOTE "+a+" "+b);
                    PIMCollection.tempNote[noteTotal]=new PIMNote(a,b);
                    PIMCollection.allNotes.add(tempNote[noteTotal]);
                    noteTotal++;
                    
                    
                    temp=br.readLine();
                    break;
                case "contact":
                    a=br.readLine();
                    b=br.readLine();
                    c=br.readLine();
                    d=br.readLine();
                    System.out.println("CONTACT "+a+" "+b+" "+c+" "+d);
                    PIMCollection.tempContact[contactTotal]=new PIMContact(a,b,c,d);
                    PIMCollection.allContacts.add(tempContact[contactTotal]);
                    contactTotal++;
                    
                    temp=br.readLine();
                    break;
                case "appointment":
                    a=br.readLine();
                    b=br.readLine();
                    c=br.readLine();
                    System.out.println("APPOINTMENT "+a+" "+b+" "+c);
                    PIMCollection.tempAppointment[appointmentTotal]=new PIMAppointment(a,b,c);
                    PIMCollection.allAppointments.add(tempAppointment[appointmentTotal]);
                    appointmentTotal++;
                    
                    temp=br.readLine();
                    break;
            }
        }
        System.out.println("************the end***************");
	}*/
	static void createTodo(String date,String text,String priority)
	{
		//System.out.println("Enter date for todo item:");
		//System.out.println("Enter todo text:");
		//System.out.println("Enter todo priority:");
		tempTodo[todoTotal]=new PIMTodo(priority,date,text);
		allTodos.add(tempTodo[todoTotal]);
		todoTotal++;
	}
	static void createNote(String priority,String text)
	{
		//System.out.println("Enter note text:");
		//String text=s.next();
		//System.out.println("Enter note priority:");
		//String priority=s.next();
		tempNote[noteTotal]=new PIMNote(priority,text);
		allNotes.add(tempNote[noteTotal]);
		noteTotal++;
	}
	static void createContact(String priority,String firstname,String lastname,String email)
	{
		//System.out.println("Enter contact priority:");
		//String priority=s.next();
		//System.out.println("Enter first name:");
		//String firstName=s.next();
		//System.out.println("Enter last name:");
		//String lastName=s.next();
		//System.out.println("Enter email address:");
		//String emailAddress=s.next();
		tempContact[contactTotal]=new PIMContact(priority,firstname,lastname,email);
		allContacts.add(tempContact[contactTotal]);
		contactTotal++;
	}
	static void createAppointment(String priority,String date,String description)
	{
		//System.out.println("Enter date for appointment item:");
		//String date=s.next();
		//System.out.println("Enter appointment descripment:");
		//String descripment=s.next();
		//System.out.println("Enter appointment priority:");
		//String priority=s.next();
		tempAppointment[appointmentTotal]=new PIMAppointment(priority,date,description);
		allAppointments.add(tempAppointment[appointmentTotal]);
		appointmentTotal++;
	}
	public static void main(String args[]) throws IOException {
		
            
            
            
            
            System.out.println("type in your name");
		user=s.next()+"/";
		init();//new some files
		load();/////////////auto loading!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		//new CalendarGUI(allTodos,allAppointments);//eifojwofijweiojifjoiwejfoiwjoifjewiof
		
		/*while(true){
			
			
	  		System.out.println("---Enter a command (suported commands are 'lists' 'create' 'save' 'load' 'show' 'quit')---");
	  		String choice=s.next();
	  		switch(choice)
	  		{
	  			case "lists":
	  				lists();
	  				continue;
	  			case "create":
	  				create();
	  				continue;
	  			case "save":
	  				save();
	  				continue;
	  			case "load":
	  				load();
	  				continue;
	  			case "quit":
	  				return;
	  			case "show":
	  			{new CalendarGUI(allTodos,allAppointments);
	  			continue;}
	  		}
	  	}*/
	}
}






