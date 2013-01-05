package com.blue.com;


import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class ListenerActivity extends Activity{

	 private UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
	 BluetoothAdapter mBluetoothAdapter;
	 BluetoothSocket socket = null;

	    BluetoothServerSocket mmServerSocket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listener);
		mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		AcceptThread ad=new AcceptThread();
		ad.start();
		Log.i("LISTEER", "AFTER THREAD");
	}
	
	
	@SuppressLint("NewApi")
	private class AcceptThread extends Thread {
	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("LISTENING NAME", uuid);
	            /*tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("LISTENING NAME", uuid);*/
	        } catch (Exception e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	       
	        Log.i("LISTENER", "THREAD CALLED");
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	            	
	            	byte[] buffer = new byte[1024];
	                int bytes;
	                socket = mmServerSocket.accept();
	                //DataInputStream din=new DataInputStream(socket.getInputStream());
	                //bytes=din.read(buffer);
	                Log.i("TAG", "PATTERN RECEIVED");
	                
	    			
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	               Log.i("MY ACTIVITY", "DETECTED");
	               
	               Intent lock = new Intent(ListenerActivity.this, LockPatternActivity.class);
	    			lock.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
	    			//Toast.makeText(getApplicationContext(), socket.getRemoteDevice().getName(), 2).show();
	    			startActivityForResult(lock, 0);
	                break;
	            }
	        }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	    }
	 
	    
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
	        switch (requestCode) {
	        case 0:
	            if (resultCode == RESULT_OK) {
	                String pattern = data.getStringExtra(LockPatternActivity._Pattern);
	                try{
	                
		            
	                	Log.i("LISTENER", "READING PATTERN "+pattern);
	                	
		            byte[] buffer = new byte[1024];
		            int bytes=socket.getInputStream().read(buffer);
		            String result=new String(buffer,0,buffer.length);
		            Log.i("TAG","PATTERN ACTUAL"+pattern);
		            Log.i("TAG","PATTERN RECEIVED"+result);
		           if(result.contains(pattern)){
		            
		            Log.i("MY AVTIVITY", "COnnECTED");
		            Toast.makeText(getApplicationContext(), "GOOD PATTERN", 2).show();
		            mmServerSocket.close();}
		           else{
		        	   setContentView(R.layout.wrongpattern);
		        	   Toast.makeText(getApplicationContext(), "WRONG PATTERN", 2).show();
		           }
	                }catch(IOException e){e.printStackTrace();}
	                
	            }
	            break;
	        }
	    }

}
