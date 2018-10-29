//Created by Efstratios Mylonas

//Import
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


//----------------------Customer-----------------------
class Customer
{
	private char[] customer_code;
	private char[] firstname;
	private char[] lastname;
	
	public Customer()
	{
		this.customer_code = new char[30];
		this.firstname = new char[30];
		this.lastname = new char[30];
	}
	
	public void setCustomer(String code, String name, String surname)
	{
		char[] c1 = code.replace("\"", "").toCharArray();
		char[] c2 = name.replace("\"", "").toCharArray();
		char[] c3 = surname.replace("\"", "").toCharArray();

		this.customer_code = c1;
		this.firstname = c2;
		this.lastname = c3;
	}
	
	public String getCode()
	{
		return new String(this.customer_code);
	}
	
	public String getFirstName()
	{
		return new String(this.firstname);
	}
	
	public String getLastName()
	{
		return new String(lastname);
	}
	
	public void printCustomer()
	{
		System.out.println(new String(this.customer_code) + " " + new String(this.firstname) + " " + new String(this.lastname));
	}

}

//-------------------Invoice Item----------------------
class Invoice_Item
{
	private char[] invoice_code;
	private char[] item_code;
	private float amount;
	private int quantity;
	
	public Invoice_Item()
	{
		invoice_code = new char[30];
		item_code = new char[100];
	}
	
	public void setInvoiceItem(String ccode, String icode, String am, String qt)
	{
		char[] c1 = ccode.replace("\"", "").toCharArray();
		char[] c2 = icode.replace("\"", "").toCharArray();
		
		this.invoice_code = c1;
		this.item_code = c2;
		
		float f = Float.parseFloat(am.replace("\"", ""));
		this.amount = f;
		
		int i = Integer.parseInt(qt.replace("\"", ""));
		this.quantity = i;
	}
	
	public String getCode()
	{
		return new String(this.invoice_code);
	}
	
	public String getItemCode()
	{
		return new String(this.item_code);
	}
	
	public String getAmount()
	{
		return Float.toString(this.amount);
	}
	
	public String getQuantity()
	{
		return Integer.toString(this.quantity);
	}
	
	public void printInvoiceItem()
	{
		System.out.println(new String(this.invoice_code) + " " + new String(this.item_code) + " " + Float.toString(amount) + " " + Integer.toString(quantity));
	}
}

//-----------------------Invoice-----------------------
class Invoice
{
	private Customer customer;
	private char[] invoice_code;
	private ArrayList<Invoice_Item> invoice_items;
	private float amount;
	private Date date;
	
	public Invoice()
	{
		customer = new Customer();
		invoice_code = new char[30];
		invoice_items = new ArrayList<Invoice_Item>();
		this.date = new Date();
	}
	
	public void setInvoice(Customer cust, ArrayList<Invoice_Item> inv_itm, String am, String dt)
	{
		this.customer = cust;
		this.invoice_items = inv_itm;
		
		char[] c1 = inv_itm.get(0).getCode().toCharArray();
		this.invoice_code = c1;
		
		float f = Float.parseFloat(am.replace("\"", ""));
		this.amount = f;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try
		{
			Date d = dateFormat.parse(dt.replace("\"", ""));
			this.date = d;
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getCustomerCode()
	{
		return this.customer.getCode();
	}
	
	public String getInvoiceCode()
	{
		return new String(this.invoice_code);
	}
	
	public String getAmount()
	{
		return Float.toString(this.amount);
	}
	
	public String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		return dateFormat.format(date);
	}
	
	public Customer getCustomer()
	{
		return this.customer;
	}
	
	public ArrayList<Invoice_Item> getInvoiceItems()
	{
		return this.invoice_items;
	}
	
	public void printInvoice()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		System.out.println(customer.getCode() + " " + this.invoice_items.get(0).getCode() + " " + Float.toString(amount) + " " + dateFormat.format(date));
	}
}

//--------------------Main Tool------------------------
public class DreamCandiesFileTool{
	
	public static void main(String[] args)
	{
		//Initialise Lists
		ArrayList<Customer> customer_list = new ArrayList<Customer>();
		ArrayList<Invoice> invoice_list = new ArrayList<Invoice>();
		ArrayList<Invoice_Item> invoice_item_list = new ArrayList<Invoice_Item>();
		ArrayList<String> customer_sample_list = new ArrayList<String>();
		
		//Set filepaths
		String CustomerCSV = "./extraction_files/customer.csv";
		String InvoiceCSV = "./extraction_files/invoice.csv";
		String InvoiceItemCSV = "./extraction_files/invoice_item.csv";
		String CustomerSampleCSV = "./input_csv/customer_sample.csv";
		
		//Set type of csv
		String line = "";
		String cvsSplitBy = ",";

		
		//Get data from the 3 files
		//1. Get customer list from file CustomerCSV
		try (BufferedReader br = new BufferedReader(new FileReader(CustomerCSV)))
		{
			while ((line = br.readLine()) != null)
			{
				Customer customer = new Customer();
				String[] index = line.split(cvsSplitBy);
				customer.setCustomer(index[0], index[1], index[2]);
				customer_list.add(customer);
			}

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		customer_list.remove(0); //remove the header
		
		//2. Get invoice item list from file InvoiceItemCSV
		try (BufferedReader br = new BufferedReader(new FileReader(InvoiceItemCSV)))
		{
			int i=0;
			while ((line = br.readLine()) != null)
			{
				if (i == 0) //get rid of header
				{
					i++;
					continue;
				}
				Invoice_Item invoice_item = new Invoice_Item();
				String[] index = line.split(cvsSplitBy);
				invoice_item.setInvoiceItem(index[0], index[1], index[2], index[3]);
				invoice_item_list.add(invoice_item);
			}

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//3. Get invoice list from file InvoiceCSV
		try (BufferedReader br = new BufferedReader(new FileReader(InvoiceCSV)))
		{
			int first=1;
			while ((line = br.readLine()) != null)
			{
				if (first == 1) //get rid of header
				{
					first = 0;
					continue;
				}
				Invoice invoice = new Invoice();
				String[] index = line.split(cvsSplitBy);
				
				//search and set corresponding customer
				Customer cust = new Customer(); 
				for (int i=0; i<customer_list.size(); i++)
				{
					if (customer_list.get(i).getCode().equals(index[0].replace("\"", "")))
					{
						cust = customer_list.get(i);
					}
				}
				
				//search and set corresponding invoice items list
				ArrayList<Invoice_Item> inv_itm = new ArrayList<Invoice_Item>();
				for (int i=0; i<invoice_item_list.size(); i++)
				{
					if (invoice_item_list.get(i).getCode().equals(index[1].replace("\"", "")))
					{
						inv_itm.add(invoice_item_list.get(i));
					}
				}
				
				invoice.setInvoice(cust, inv_itm, index[2], index[3]);
				invoice_list.add(invoice);
			}

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		
		//Get customer sample customers list from file CustomerSampleCSV
		try (BufferedReader br = new BufferedReader(new FileReader(CustomerSampleCSV)))
		{
			int i=0;
			while ((line = br.readLine()) != null)
			{
				if (i == 0)
				{
					i++;
					continue;
				}
				String customer_sample;
				customer_sample = line.replace("\"", "");
				customer_sample_list.add(customer_sample);
			}

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//Make output empty lists for the output of the tool
		ArrayList<Customer> output_customer_list = new ArrayList<Customer>();
		ArrayList<Invoice> output_invoice_list = new ArrayList<Invoice>();
		ArrayList<Invoice_Item> output_invoice_item_list = new ArrayList<Invoice_Item>();
		
		//Fill the lists by searching the customers we've got in the customer sample file
		//Once we found the customer we get the rest of data in each list
		for (int i=0; i<customer_sample_list.size(); i++)
		{
			String search = customer_sample_list.get(i); //set the customer we want to search
			for (int j=0; j<invoice_list.size(); j++)
			{
				if (search.equals(invoice_list.get(j).getCustomerCode()))
				{
					output_invoice_list.add(invoice_list.get(j));
					int found=0;
					for (int k=0; k<output_customer_list.size(); k++) //check for duplicates
					{
						if (invoice_list.get(j).getCustomerCode().equals(output_customer_list.get(k).getCode()))
						{
							found = 1;
						}
					}
					if (found == 0)
					{
						output_customer_list.add(invoice_list.get(j).getCustomer());
					}
					ArrayList<Invoice_Item> temp_invoice_item_list = new ArrayList<Invoice_Item>();
					temp_invoice_item_list = invoice_list.get(j).getInvoiceItems();
					for (int k=0; k<temp_invoice_item_list.size(); k++)
					{
						output_invoice_item_list.add(temp_invoice_item_list.get(k));
					}
				}
			}
		}
		
		//Make output files
		//But First, create output folder if it doesn't exists
		String outputPath = "./output_csv/";
		File directory = new File(outputPath);
		if (!directory.exists())
		{
			directory.mkdir();
		}
		
		//Then we create and fill the output customer file in ASCII encoding
		try {
			File fout = new File("./output_csv/customer.csv");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "US-ASCII"));
			bw.write("\"CUSTOMER_CODE\",\"FIRSTNAME\",\"LASTNAME\"");
			bw.newLine();
			for (int i=0; i<output_customer_list.size(); i++)
			{
				String s = 
					"\"" + 
					output_customer_list.get(i).getCode() + 
					"\",\"" + 
					output_customer_list.get(i).getFirstName() + 
					"\",\"" +
					output_customer_list.get(i).getLastName() +
					"\"";
					
				bw.write(s);
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}

		//Next the invoice item file
		try {
			File fout = new File("./output_csv/invoice_item.csv");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "US-ASCII"));
			bw.write("\"INVOICE_CODE\",\"ITEM_CODE\",\"AMMOUNT\",\"QUANTITY\"");
			bw.newLine();
			for (int i=0; i<output_invoice_item_list.size(); i++)
			{
				String s = 
					"\"" + 
					output_invoice_item_list.get(i).getCode() + 
					"\",\"" + 
					output_invoice_item_list.get(i).getItemCode() + 
					"\",\"" +
					output_invoice_item_list.get(i).getAmount() +
					"\",\"" +
					output_invoice_item_list.get(i).getQuantity() +
					"\"";
					
				bw.write(s);
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//And lastly the invoice file
		try {
			File fout = new File("./output_csv/invoice.csv");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "US-ASCII"));
			bw.write("\"CUSTOMER_CODE\",\"INVOICE_CODE\",\"AMMOUNT\",\"DATE\"");
			bw.newLine();
			for (int i=0; i<output_invoice_list.size(); i++)
			{
				String s = 
					"\"" + 
					output_invoice_list.get(i).getCustomerCode() + 
					"\",\"" + 
					output_invoice_list.get(i).getInvoiceCode() + 
					"\",\"" +
					output_invoice_list.get(i).getAmount() +
					"\",\"" +
					output_invoice_list.get(i).getDate() +
					"\"";
					
				bw.write(s);
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
}