package com.blue.com;



import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SendRequestActivity extends Activity implements OnItemClickListener{

	 BluetoothSocket mmSocket;
     BluetoothDevice mmDevice;
	LazyAdapter adapter;
	ListView lv;
	ArrayList<String> devices;
	ArrayList<BluetoothDevice> btdevice;
	BluetoothAdapter bt;
	BluetoothSocket bs;
	 SingBroadcastReceiver mReceiver;
	 private OutputStream outStream = null;
	 private UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendrequest);
		
		lv=(ListView)findViewById(R.id.listView);
		devices=new ArrayList<String>();
		adapter = new LazyAdapter(this,devices,lv);
		btdevice=new ArrayList<BluetoothDevice>();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		bt=BluetoothAdapter.getDefaultAdapter();
		
		Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBT, 0xDEADBEEF);
        if (bt.isDiscovering()){
            bt.cancelDiscovery();
        }
        bt.startDiscovery();
        mReceiver = new SingBroadcastReceiver();
        IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, ifilter);
	}
	
	private class SingBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //may need to chain this to a recognizing function
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a Toast
               
                String derp = device.getName() + " - " + device.getAddress();
                if(!devices.contains(derp)){
                devices.add(derp);
               btdevice.add(device);
               Log.i("TAG", derp);
                }
               
                try{
                
                lv.setAdapter(adapter);
     			
                 adapter.notifyDataSetChanged();
                }catch(Exception e){}
                
           
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            	
            	 Log.i("TAG", "NOT_FOUND");
            	 
     			
            
            }
        }
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		Toast.makeText(this,devices.get(arg2),2).show();
		final BluetoothSocket mmSocket;
	     final BluetoothDevice mmDevice;
		
		//mmDevice=btdevice.get(arg2);
		//mmSocket=mmDevice.createRfcommSocketToServiceRecord(uuid);
		//bt.cancelDiscovery();
		//mmSocket.connect();
		ConnectThread c=new ConnectThread(btdevice.get(arg2));
		c.start();
		//bt.cancelDiscovery();
		
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
	        case 0:
	            if (resultCode == RESULT_OK) {
	                String pattern = data.getStringExtra(LockPatternActivity._Pattern);
	                Toast.makeText(getApplicationContext(), pattern, 2).show();
	                try{
	                
		            OutputStream ps=mmSocket.getOutputStream();
		            byte[] a = pattern.getBytes();
		            ps.write(a);
		            
		            Log.i("TAG","PATTERN SENT:"+pattern);
		            Log.i("MY AVTIVITY", "COnnECTED"+new String(a,0,a.length));
	                }catch(IOException e){e.printStackTrace();}
	                
	            }
	            break;
	        }
	    }
	@SuppressLint("NewApi")
	private class ConnectThread extends Thread {
	    
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	           tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
	           //tmp=device.createInsecureRfcommSocketToServiceRecord(uuid);
	        	//Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	        	//tmp = (BluetoothSocket) m.invoke(device, 1);
	        } catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        
	 
	        // Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			//bt.cancelDiscovery();
	    	try {
				mmSocket.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent lock = new Intent(SendRequestActivity.this, LockPatternActivity.class);
			lock.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
			startActivityForResult(lock, 0);
	 
	        // Do work to manage the connection (in a separate thread)
	       
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	            unregisterReceiver(mReceiver);
	        } catch (IOException e) { }
	    }
	}

}
