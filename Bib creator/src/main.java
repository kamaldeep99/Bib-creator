import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

//@author 
//		  Kamaldeep Singh


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		Integer latexFileNum = 1;
		int exist = 0;
		PrintWriter writeFile;
		int deletedfiles=0;

		System.out.println("Welcome to Bib Creator!\n") ;

		// search searchFilename for open any file after execution

		Scanner scanner = new Scanner(System.in);

		// ARRAY to store 30 files with IEEE,ACM, NJ extention
		String searchFilename;
		String []searchFilenameArray= new String[30];

		// Checking if all 10 Latex Files exists
		// exit program if any of the 10 file missing

		for(latexFileNum=1;latexFileNum<11;latexFileNum++){
			File myFile = new File("Latex"+latexFileNum.toString()+".bib");

			if(myFile.exists()==false)
			{
				System.out.println("Could not open input file latex"+ latexFileNum + ".bib for reading") ;
				System.out.println("Please check if file exist program will terminate after opening any input files.");
				exist=1;
				break;
			}
		}

		// if all files exists.. 

		if (exist==0) {
			for(latexFileNum = 1; latexFileNum < 11; latexFileNum++) {
				try {

					// Creating 30 files 
					writeFile = new PrintWriter(new FileOutputStream("IEEE"+latexFileNum.toString()+".json"));
					writeFile = new PrintWriter(new FileOutputStream("ACM"+latexFileNum.toString()+".json"));
					writeFile = new PrintWriter(new FileOutputStream("NJ"+latexFileNum.toString()+".json"));

					searchFilenameArray[latexFileNum-1]="IEEE"+latexFileNum.toString()+".json";
					searchFilenameArray[10+latexFileNum-1]="ACM"+latexFileNum.toString()+".json";
					searchFilenameArray[20+latexFileNum-1]="NJ"+latexFileNum.toString()+".json";

				} catch (FileNotFoundException e) {
					e.printStackTrace();

				}

			}

		}

		latexFileNum = 1;

		//READING AND WRITING ALL LATEX FILES
		while(latexFileNum < 11 && exist==0) {

			String	name;
			int articleCount = 0;
			int fieldCount = 0;

			// array to store all data and filtered data
			ArrayList<ArrayList<String>> allData = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> filteredData = new ArrayList<ArrayList<String>>();

			try {

				name="Latex"+latexFileNum.toString()+".bib";
//				System.out.println("\n");

//				System.out.println("opening file latex "+ latexFileNum);

				File file = new File(name);

				// to read files
				BufferedReader br =null;
				PrintWriter pr = null;

				br = new BufferedReader(new FileReader(file));

				// storing ASCI Value of file reader
				int c = 0; 
				String line= "";
				int s=0;
				int flag=0;

				// Read character by character
				while ((c = br.read()) != -1) {
					char ch = (char) c;
					line = line + ch;

					if(line.contains("@ARTICLE{"))
					{
						flag=1;
						fieldCount=0;	
					}

					// start counting of line
					if ((ch=='a'||ch=='j'||ch=='t'||ch=='y'||ch=='p'||ch=='n'||ch=='I'||ch=='m'||ch=='v'||ch=='k'||ch=='d')&&(flag==1)) {

						line = "";
						line = line+ch;

						while ((s = br.read()) != -1) {
							char sh = (char) s;

							if(sh==',') {

								fieldCount++;

								allData.add(new ArrayList<String>());
								allData.get(articleCount).add(fieldCount-1, line);

								//			        		System.out.println(allData.get(articleCount).get(fieldCount-1));

								// total 11 fields of one article
								if (fieldCount%11==0) {
									flag = 0;
									line = "";
									fieldCount = 0;
									articleCount++;

								}
								break;


							}else {
								line=line+sh;
							}

						}

					} 

				}
				// ending while loop

				br.close();	
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				System.out.println("File does not exist, please, check the project location.");
				System.exit(0);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for(int t=0;t<articleCount;t++){ 

				String temp="";

				for(int d=0;d<allData.get(t).size()-1;d++) {
					for(int f=d+1;f<allData.get(t).size();f++) {

						int comparison = allData.get(t).get(d).substring(0,1).toLowerCase().compareTo(allData.get(t).get(f).substring(0,1).toLowerCase());
						//System.out.println(data.get(t).get(d).substring(0,1)+t+""+d+"  "+t+""+f+data.get(t).get(f).substring(0,1);
						//System.out.println(comparison);

						if(comparison > 0) {
							temp=allData.get(t).get(d);
							allData.get(t).set(d,allData.get(t).get(f));
							allData.get(t).set(f,temp);
						}
					}

				}

			}
			// row- article count

			for (int i = 0; i < articleCount; i++) {

				// column article fields
				for (int j = 0; j <allData.get(i).size(); j++) {

					int k = allData.get(i).get(j).indexOf('{');

					name=allData.get(i).get(j).substring(k+1,allData.get(i).get(j).length()-1);
					filteredData.add(new ArrayList<String>());
					filteredData.get(i).add(j,name);

				}
			}

			int errorflag=0;

			for (int i = 0; i < filteredData.size(); i++) {

				if(errorflag==1) {
					break;
				}
				for (int j = 0; j < filteredData.get(i).size(); j++) { 


					if(filteredData.get(i).get(j)=="") {

						deletedfiles++;
						if(j==0){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"author\" is Empty. Processing stopped at this point.Other empty fields may be present as well!");
							errorflag=1;


							break;
						}
						if(j==1){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"DOI\" is Empty. Processing stopped at this point.Other empty fields may be present as well!");
							errorflag=1;
							break;
						}
						if(j==2)
						{
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"ISSN\" is Empty. Processing stopped at this point. Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==3)
						{
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));

							System.out.println("File is Invalid: Field \"journal\" is Empty. Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==4){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"keywords\" is Empty. Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==5)
						{
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"month\" is Empty. Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==6){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"number\" is Empty.Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==7){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"pages\" is Empty. Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==8){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"title\" is Empty. Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==9){
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number "+(i+1));
							System.out.println("File is Invalid: Field \"volume\" is Empty. Processing stopped at this point.Other empty fields may be present as well ");
							errorflag=1;
							break;
						}
						if(j==10)
						{
							System.out.println("Error: Error detected empty fields!");
							System.out.println("=====================================");

							System.out.println("\nProblem detected with input file: latex"+latexFileNum+".bib"+" on "+"article number fdd"+(i+1));
							System.out.println("File is Invalid: Field \"year\" is Empty. Processing stopped at this point.Other empty fields may be present as well");
							errorflag=1;
							break;
						}

					} 

				} 


			}

			// deleting invalid files

//			System.out.println("\n");
			if(errorflag==1) {
				File deletefile = new File("IEEE"+latexFileNum.toString()+".json"); 
				if (deletefile.delete()) { 
//					System.out.println("Deleted the file: " + deletefile.getName());

				}
				deletefile = new File("ACM"+latexFileNum.toString()+".json");
				if (deletefile.delete()) { 
//					System.out.println("Deleted the file: " + deletefile.getName());

				}

				deletefile = new File("NJ"+latexFileNum.toString()+".json"); 
				if (deletefile.delete()) { 
//					System.out.println("Deleted the file: " + deletefile.getName());

				}

			}

			// if no error start writing into different format files
			if(errorflag==0){	

				StringBuilder one = new StringBuilder();
				String[][] formats = new String[articleCount][3];


				for(int i=0;i<articleCount;i++) {
					//System.out.println(filteredData.get(i).get(0)); 
					int counttokens;
					String oneLine = filteredData.get(i).get(0);
					String and ="and";
					oneLine=oneLine.replace("and", "&");
					one.setLength(0);


					//System.out.println(oneLine);

					StringTokenizer str = new StringTokenizer(oneLine, "&");


					counttokens=str.countTokens();
					String[] authorname = new String[counttokens];



					for (int j = 0; j <authorname.length; j++) { 

						authorname[j]=str.nextToken();
						one.append(authorname[j]);

						if(j<(authorname.length-1))

						{one.append(",");

						}

					}
					// printing formats

					//IEEE
					one.append("."+"\""+filteredData.get(i).get(8)+"\""+","+filteredData.get(i).get(3)+","+"vol. "+filteredData.get(i).get(9)+","+"no. "+filteredData.get(i).get(6)+","+"p. "+filteredData.get(i).get(7)+","+filteredData.get(i).get(5)+" "+filteredData.get(i).get(10)+".");
					formats[i][0]=one.toString();

					one.setLength(0);

					// ACM
					one.append(authorname[0]+"et al."+filteredData.get(i).get(10)+"."+filteredData.get(i).get(8)+"."+filteredData.get(i).get(3)+"."+filteredData.get(i).get(9)+","+filteredData.get(i).get(6)+"("+filteredData.get(i).get(10)+")"+","+filteredData.get(i).get(7)+". DOI:https://doi.org/"+filteredData.get(0).get(1)+".");
					formats[i][1]=one.toString();

					one.setLength(0);

					// NJ
					one.append(oneLine+"."+" "+filteredData.get(i).get(8)+"."+filteredData.get(i).get(3)+"."+filteredData.get(i).get(9)+","+filteredData.get(i).get(7)+"("+filteredData.get(i).get(10)+").");
					formats[i][2]=one.toString();

					one.setLength(0);

				}

				FileWriter fr = null;
				BufferedWriter br = null;

				// WRITE FILES
				for(int i=0;i<formats.length;i++)
				{
					for(int j=0;j<formats[i].length;j++)
					{

						if(j==0) {

							try {

								fr = new FileWriter("IEEE"+latexFileNum.toString()+".json", true);
								br = new BufferedWriter(fr);
								writeFile= new PrintWriter(br);

								writeFile.println(formats[i][j]);

								writeFile.close();
								br.close();
								fr.close();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if(j==1) {

							try {
								fr = new FileWriter("ACM"+latexFileNum.toString()+".json", true);
								br = new BufferedWriter(fr);
								writeFile= new PrintWriter(br);

								writeFile.println("["+(i+1)+"] "+formats[i][j]);

								writeFile.close();
								br.close();
								fr.close();


							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if(j==2) {

							try {

								fr = new FileWriter("NJ"+latexFileNum.toString()+".json", true);
								br = new BufferedWriter(fr);
								writeFile= new PrintWriter(br);

								writeFile.println(formats[i][j]);

								writeFile.close();
								br.close();
								fr.close();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

			}

			latexFileNum ++;	
		}

		// show number of deleted files
		if(exist==0){
			System.out.println("\nA total of "+deletedfiles+" were invalid, and cannot be processed. All other "+ (10-deletedfiles)+" \"valid\" files have been created.");
		}

		int found=0;
		int attempt=0;
		System.out.println("\n");
		while(attempt<2 && exist==0)
		{
			System.out.print("Please enter name of one of the file that you need to review: ");
			searchFilename=scanner.next();
			File find= new File(searchFilename);
			 {

				if(find.exists()==false){
					if(attempt==0) {
					System.out.println("Could not open input file. File does not exist; possibly it coud not be created!!");
					System.out.println("\nHowever, you will be allowed another chance to enter another file name.");}
					else {
						System.out.println("Could not open input file again!. either File does not exist or possibly it coud not be created!!");
						System.out.println("Sorry I am unable to display desired files the program will exit!");
						
					}
					//						break;
					//					for(int i =0;i<30;i++) {
					//						
					//						if(searchFilename.equals(searchFilenameArray[i]))
					//						{
					//							System.out.println("Could not open input file. File does not exist; possibly it coud not be created!!");
					//							System.out.println("\nHowever, you will be allowed another chance to enter another file name.");
					//							break;
					//	
					//						}
					////					else if(i==29){
					////						System.out.println("Could not open input file Either file does not exist possibly it coud not be created.");
					////						System.out.println("\nHowever, you will be allowed another chance to enter another file name.");
					////
					////
					////						}
					//					}
				}
				else {
					try {

						File file = new File(searchFilename);

						BufferedReader br =null;


						br=new BufferedReader(new FileReader(file)); 
						String c = ""; 

						// Read character by character
						while((c = br.readLine()) != null)
						{	
							// convert the integer to char

							System.out.println(c+"\n");
						}
						br.close();
						Desktop desktop = null;
						
						if (Desktop.isDesktopSupported()) {
							desktop = Desktop.getDesktop();
							desktop.open(new File(searchFilename));
							
							System.out.println("\nGoodbye! Hope you have enjoyed creating files using BibCreator.");
							System.exit(0);

						}

						desktop.open(new File(searchFilename));
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					found=1;
				}
			}
//			else {
//
//
//				if(find.exists()==false) {
//					System.out.println("Could not open input file again. File does not exist possibly it coud not be created.");
//					System.out.println("Sorry! I am unable to display desired files! Program will exit!");
//					
////					for(int i =0;i<30;i++) {
////						if(searchFilename.equals(searchFilenameArray[i]))
////						{
////							System.out.println("Could not open input file again. File does not exist possibly it coud not be created as it was deleted after creation due to errors");
////							System.out.println("Sorry I am unable to display desired files the program will exit!");
////							break;
////
////
////						}
////						else if(i==29){
////							System.out.println("Couldnot open input file again .File doesnot exist possibly it could not be created as file never existed");
////							System.out.println("Sorry I am unable to display desired files the program will exit!");
////							System.exit(0);
////
////
////						}
////					}
//				}
//				else {
//					found=1;
//					try {
//
//						File file = new File(searchFilename);
//						BufferedReader br =null;
//
//						br=new BufferedReader(new FileReader(file)); 
//						String c = ""; 
//
//						// Read character by character
//						while((c = br.readLine()) != null)
//						{	
//							// convert the integer to char
//
//							System.out.println(c+"\n");
//						}
//						br.close();
//
//						Desktop desktop = null;
//						if (Desktop.isDesktopSupported()) {
//							desktop = Desktop.getDesktop();
//							desktop.open(new File(searchFilename));
//							System.out.println("\nGoodbye! hope you have enjoyed creating files using bib creator");
//							System.exit(0);
//						}
//						desktop.open(new File(searchFilename));
//					} catch (IOException ioe) {
//						ioe.printStackTrace();
//					}
//
//				}
//			}
			attempt++;
		}

	}

}
