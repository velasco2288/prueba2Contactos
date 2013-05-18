package com.example.contactos;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "datos.db";
       private static final String TABLE_CONTACTS = "contacts";
 
    // Columnas de la tabla
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
 
    //constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    
    /*creamos la tabla */ 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("+ KEY_ID + " Integer PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // te crea por efecto el asistente
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       
				// Se borra la tabla 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Se llama al m�todo donde hemos definido la creaci�n de las tablas.
		onCreate(db);

    	   }
    
   
    
    // agregamos contacto
    public void addContact(Contact contact) {
    
    	
    	SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); 
        values.put(KEY_PH_NO, contact.getPhoneNumber()); 
     
        // insertamos la fila
        db.insert(TABLE_CONTACTS, null, values);
        
        
        db.close(); 
    }  
  
    
    
    
    
    // buscamos un contacto por ID
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2));
        
        return contact;
    } 
   
    
    
    // OBTENEMOS LA LISTA DE CONTACTOS
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
       
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
       
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // AGREGAMOS EL CONTACTO A LA LISTA
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
     
       //RETORNAMOS LA LISTA DE CONTACTOS
        return contactList;
    } 
   
   
    
   // ACTUALIZAMOS CONTACTO
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
     
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
    
   
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
        new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    /*  
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }*/
 

}