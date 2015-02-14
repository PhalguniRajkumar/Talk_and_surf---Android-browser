package com.rajkumar.talkandsurf;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Bookmark extends Activity{

	SQLiteDatabase book;
	Cursor c;
	int count,i=0;
	String b[];
	ArrayAdapter<String> ob;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		lv=(ListView)findViewById(R.id.lv);
 		
		book=openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);

		
		//creating table
		try{
		book.execSQL("create table bookmark (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,url varchar(100));");
		Toast.makeText(this, "Table Created", Toast.LENGTH_LONG).show();
		}catch(Exception e){
			Toast.makeText(this, "Table already exist", Toast.LENGTH_LONG).show();
		}
		
		c=book.rawQuery("select * from bookmark;",null);
		count=c.getCount();
	//	Toast.makeText(Reminder.this,""+count,Toast.LENGTH_LONG).show();
		b=new String[count];
			while(c.moveToNext())
			{
				b[i]=c.getString(1);
				i++;
			}
		//	Toast.makeText(Reminder.this,details[0],Toast.LENGTH_LONG).show();
			ob=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,b);
			lv.setAdapter(ob);
	
			
			
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
    			{
    				String date=b[1];
    				Intent i=new Intent(Bookmark.this,WebViewActivity.class);
    				i.putExtra("ur",date);
    				startActivity(i);
    				finish();
    			}
    		});
			
			//For delete option
		    registerForContextMenu(lv);
	}
	//For delete option
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	MenuInflater mi = getMenuInflater();
	mi.inflate(R.menu.brow_delete_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			  //int menuItemIndex = item.getItemId();
			 // String[] menuItems = getResources().getStringArray(R.array.menu);
			 // String menuItemName = menuItems[menuItemIndex];
			  String listItemName = b[info.position];
			 // Toast.makeText(Bookmark_Activity.this,listItemName,Toast.LENGTH_LONG).show();
			  try
				{
					book.execSQL("delete from bookmark where url='"+listItemName+"';");
					Toast.makeText(Bookmark.this,"Bookmark deleted.",Toast.LENGTH_LONG).show();
				}	
				catch(Exception e)
				{
					e.printStackTrace();
				}
			  
			  //Display new list
			  Cursor c=book.rawQuery("select * from bookmark;",null);
				count=c.getCount();
				int i=0;
			//	Toast.makeText(Reminder.this,""+count,Toast.LENGTH_LONG).show();
				String[] st=new String[count];
					while(c.moveToNext())
					{
						st[i]=c.getString(1);
						i++;
					}
					//Toast.makeText(Reminder.this,details[0],Toast.LENGTH_LONG).show();
					ob=new ArrayAdapter<String>(Bookmark.this,android.R.layout.simple_list_item_1,st);
					lv.setAdapter(ob);
		  return true;
	}
	public void add_book(View v){
		
		Intent in=getIntent();
		Bundle bun=in.getExtras();
		String book_url=bun.getString("m");
		//inserting new entry into the table
				try{
					book.execSQL("insert into bookmark (url) values ('"+book_url+"');");
					Toast.makeText(this, "Bookmark saved", Toast.LENGTH_LONG).show();
					
				}catch(Exception ex)
				{	
					Toast.makeText(this, "Error in saving bookmark", Toast.LENGTH_LONG).show();
				}
		
				finish();
	}
}

