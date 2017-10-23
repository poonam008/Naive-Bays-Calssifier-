// Group No. 24
// 1611MC07 Poonam


import java.util.*;
import java.io.*;
public class naive_base
{
	public static void main(String[] args)throws IOException, FileNotFoundException
	{
		FileReader fileReader = new FileReader(new File("train_full_B.tsv"));
		BufferedReader br = new BufferedReader(fileReader);
		FileWriter fileWriter = new FileWriter(new File("Test-result.txt"));
		BufferedWriter bw = new BufferedWriter(fileWriter);
		String line = null;
		int i=0;
		HashMap<String, Integer> p = new HashMap<String, Integer>();
		HashMap<String, Integer> n = new HashMap<String, Integer>();
		HashMap<String, Integer> nr = new HashMap<String, Integer>();
		HashSet<String> vocabulary = new HashSet<String>();
		long pc=0,nc=0,nrc=0;
		while ((line = br.readLine()) != null)
		{
			String[] arrayst = line.split("\\s+",0);
																				//for positive tweets
			if(arrayst[2].equals("positive"))
			{
				pc++;
				for(int j=3;j<arrayst.length;j++)
				{
					vocabulary.add(arrayst[j]);									//maintaining vocabulary
					if(p.containsKey(arrayst[j]))								//maintaining positive hashmap
					{
						p.put(arrayst[j],p.get(arrayst[j]) + 1);
					}
					else
					{
						p.put(arrayst[j],1);
					}
				}
			}
			else
				if(arrayst[2].equals("negative"))
				{
					nc++;
					for(int j=3;j<arrayst.length;j++)
					{
						vocabulary.add(arrayst[j]);								//maintaining vocabulary
						if(n.containsKey(arrayst[j]))							//maintaining negative hashmap
						{
							n.put(arrayst[j],n.get(arrayst[j]) + 1);
						}
						else
						{
							n.put(arrayst[j],1);
						}
					}
				}
				else
				{
					nrc++;
					for(int j=3;j<arrayst.length;j++)
					{
						vocabulary.add(arrayst[j]);								//maintaining vocabulary
						if(nr.containsKey(arrayst[j]))							//maintaining neutral hashmap
						{
							nr.put(arrayst[j],nr.get(arrayst[j]) + 1);
						}
						else
						{
							nr.put(arrayst[j],1);
						}
					}
				}
			i++;
		}
		long tpc=0,tnc=0,tnrc=0;
		for (HashMap.Entry<String,Integer> entry : p.entrySet())			//total positive count
		{
		    tpc=(tpc + entry.getValue());
		}
		for (HashMap.Entry<String,Integer> entry : n.entrySet())			//total negative count
		{
		    tnc=(tnc + entry.getValue());
		}
		for (HashMap.Entry<String,Integer> entry : nr.entrySet())			//total neutral count
		{
		    tnrc=(tnrc + entry.getValue());
		}
		System.out.println("TPC = "+tpc);
		System.out.println("TNC = "+tnc);
		System.out.println("TNRC = "+tnrc);
		fileReader = new FileReader(new File("Test-set.txt"));
		br = new BufferedReader(fileReader);
		line = null;
		long vocsize=vocabulary.size();
		double v1,v2,pp,pn,pnt;
		long x=0;
		double p1,p2,p3;
		while ((line = br.readLine()) != null)
		{
			pp = (double)pc/(pc+nc+nrc);
			pn = (double)nc/(pc+nc+nrc);
			pnt = (double)nrc/(pc+nc+nrc);
			p1=Math.log(pp);p2=Math.log(pn);p3=Math.log(pnt);
			String[] arrayst = line.split("\\s+",0);
			for(int j=3;j<arrayst.length;j++)										//calculating positive prob
			{
				if(p.containsKey(arrayst[j]))
				{
					p1 = p1 + Math.log(p.get(arrayst[j]) + 1) - Math.log(tpc+vocsize);
				}
				else
				{
					p1 = p1 - Math.log(tpc+vocsize);
				}
			}
			for(int j=3;j<arrayst.length;j++)										//calculation negative count
			{
				if(n.containsKey(arrayst[j]))
				{
					p2 = p2 + Math.log(n.get(arrayst[j]) + 1) - Math.log(tnc+vocsize);
				}
				else
				{
					p2 = p2 - Math.log(tnc+vocsize);
				}
			}
			for(int j=3;j<arrayst.length;j++)										//calculation neutral count
			{
				if(nr.containsKey(arrayst[j]))
				{
					p3 = p3 + Math.log(nr.get(arrayst[j]) + 1) - Math.log(tnrc+vocsize);
				}
				else
				{
					p3 = p3 - Math.log(tnrc+vocsize);
				}
			}
			System.out.println(p1+" "+p2+" "+p3);
			if(p1>=p2)
			{
				if(p3>p1)
				{
					arrayst[2]="neutral";
				}
				else
				{
					arrayst[2]="positive";
				}
			}
			else
			{
				if(p3>p2)
				{
					arrayst[2]="neutral";
				}
				else
				{
					arrayst[2]="negative";
				}
			}
			for(int j=0;j<=2;j++)													//writing testing file
			{
				bw.write(arrayst[j]+"\t");
			}
			for(int j=3;j<arrayst.length;j++)
			{
				bw.write(arrayst[j]+" ");
			}
			bw.write("\n");
		}
		bw.close();
		br.close();
	}
}
