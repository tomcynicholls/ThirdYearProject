import java.util.Scanner;


public class test {

	public static void main(String[] args) {
		
		/*String ip = "127.0.0.1";
		System.out.println(ip);
		char[] charip = ip.toCharArray();
		char[] fcharip = new char[charip.length];
		int count = 0;
		
		for (int i = 0; i<charip.length; i++) {
			if (charip[i] != '.') { 
				fcharip[count] = charip[i];
				count++;
			}
		}
		
		String finalip = String.valueOf(fcharip);
		System.out.println(finalip);
		
		String trimmed = finalip.trim();
		
		Long result = Long.valueOf(trimmed);
		System.out.println(result);*/
		
		/*Scanner in = new Scanner(System.in);
  		String s;
  		System.out.println("Do you have a username? [y/n]");
  		s = in.nextLine();
		String u = isUser(s);
		System.out.println(u);
  		
	}
	
	public static String isUser(String u){
		Scanner in = new Scanner(System.in);
		System.out.println("u is: " + u);
		if ((!(u.equals("y"))) || (!(u.equals("n")))) {
				System.out.println("Please enter y or n");
				return isUser(in.nextLine());
		}
		else {
			return u;
		}*/
		
		//THIS WORKS
		String s;
		Scanner in = new Scanner(System.in);
		while(true) {
  		System.out.println("Do you have a username? [y/n]");
  		s = in.nextLine();
		
  		if (s.equals("y")){
  			System.out.println("YES");
  			
  			break;
  		}
  		else {
  			if (s.equals("n")) {
  				System.out.println("NO");
  				break;
  			}
  			else {
  				System.out.println("Please enter y or n");
  			}
  		}
  		
		}
	}
}
