package com.md4n1.bluetootharduino;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Controls extends Activity
{

    private static final UUID MAGIC_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // magic
    //private static final UUID MAGIC_UUID = UUID.fromString("72878edc-b023-11e3-88fe-425861b86ab6"); // magic
												 // UUID
    private static final String TAG = Main.class.getName();

    private BluetoothAdapter bluetoothAdapter;
    private BTConnectionThread btConnectionThread;

    private ProgressDialog pd;

    private TextView result;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float x, y, z;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.controls);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

        result = (TextView) findViewById(R.id.result);
        result.setText("No result yet");

	    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	    bindViews();

    	BluetoothDevice device = (BluetoothDevice) getIntent().getParcelableExtra("device");
	    if(device != null)
        {
	      connectToBTDevice(device);
	    }
        else
        {
	      Toast.makeText(this, "There is no chosen device", Toast.LENGTH_SHORT).show();
	    }
    }

     private void refreshDisplay()
     {
		String output = String.format("X = %f  Y = %f", x, y);
		result.setText(output);
     }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(accelerationListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop()
    {
        sensorManager.unregisterListener(accelerationListener);
        super.onStop();
    }

      private SensorEventListener accelerationListener = new SensorEventListener()
      {
        @Override
		public void onAccuracyChanged(Sensor sensor, int acc)
        {
		}

        @Override
		public void onSensorChanged(SensorEvent event)
        {
            x = event.values[0];
			y = event.values[1];
			z = event.values[2];
            refreshDisplay();
		}

	};

    private void bindViews()
    {
	    ((ToggleButton) findViewById(R.id.Forward1)).setOnCheckedChangeListener(changeListener);
	    ((ToggleButton) findViewById(R.id.Forward2)).setOnCheckedChangeListener(changeListener);
	    ((ToggleButton) findViewById(R.id.Forward3)).setOnCheckedChangeListener(changeListener);
	    ((ToggleButton) findViewById(R.id.Forward4)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Forward5)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Forward6)).setOnCheckedChangeListener(changeListener);

        ((ToggleButton) findViewById(R.id.Reverse1)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Reverse2)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Reverse3)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Reverse4)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Reverse5)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Reverse6)).setOnCheckedChangeListener(changeListener);

        ((ToggleButton) findViewById(R.id.Up)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Down)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Left)).setOnCheckedChangeListener(changeListener);
        ((ToggleButton) findViewById(R.id.Right)).setOnCheckedChangeListener(changeListener);

    }

    protected void connectToBTDevice(final BluetoothDevice device)
    {
	    new AlertDialog.Builder(this).setMessage("Connect to " + device.getName() + " ?").setNegativeButton("No", null).setPositiveButton("Yes", new OnClickListener()
        {
	         @Override
	         public void onClick(DialogInterface dialog, int which)
             {
		         pd = new ProgressDialog(Controls.this);
		         pd.setMessage("Waiting BT Device");
		         pd.setCancelable(true);
		         pd.setOnCancelListener(new OnCancelListener()
                 {

		           @Override
		           public void onCancel(DialogInterface dialog)
                   {
		         	    if (btConnectionThread != null)
                        {
			                 btConnectionThread.cancel();
			            }
		            }
		            });
		            pd.show();

		            btConnectionThread = new BTConnectionThread(device);
		            btConnectionThread.start();
	          }
	        }).create().show();

    }

    private OnCheckedChangeListener changeListener = new OnCheckedChangeListener()
    {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
	        String pin = "";
	         if (buttonView.getId() == R.id.Forward1)
             {
		        pin = "A";

	         }
             else if (buttonView.getId() == R.id.Forward2)
             {
		        pin = "B";
	         }
             else if (buttonView.getId() == R.id.Forward3)
             {
		        pin = "C";
	         }
	         else if (buttonView.getId() == R.id.Forward4)
             {
                 pin = "D";
             }
             else if (buttonView.getId() == R.id.Forward5)
             {
                 pin = "E";
             }
             else if (buttonView.getId() == R.id.Forward6)
             {
                 pin = "F";
             }
             else if (buttonView.getId() == R.id.Reverse1)
             {
                 pin = "G";
             }
             else if (buttonView.getId() == R.id.Reverse2)
             {
                 pin = "H";
             }
             else if (buttonView.getId() == R.id.Reverse3)
             {
                 pin = "I";
             }
             else if (buttonView.getId() == R.id.Reverse4)
             {
                 pin = "J";
             }
             else if (buttonView.getId() == R.id.Reverse5)
             {
                 pin = "K";
             }
             else if (buttonView.getId() == R.id.Reverse6)
             {
                 pin = "L";
             }
             else if (buttonView.getId() == R.id.Up)
             {
                 pin = "M";
             }
             else if (buttonView.getId() == R.id.Down)
             {
                 pin = "N";
             }
             else if (buttonView.getId() == R.id.Left)
             {
                 pin = "P";
             }
             else if (buttonView.getId() == R.id.Right)
             {
                 pin = "O";
             }
            if (isChecked)
             {
		        btConnectionThread.write((pin).getBytes());
             } else
             {
		        btConnectionThread.write((pin).getBytes());
	         }
        }
    };

    class BTChannelThread extends Thread
    {
	    private boolean keepAlive = true;
	    private OutputStream outStream;
    	private BluetoothSocket btSocket;

	    public BTChannelThread(BluetoothSocket btSocket)
        {
	      this.btSocket = btSocket;

	      try
          {
		    outStream = btSocket.getOutputStream();
	      } catch (IOException e)
           {
	       }
	     }

	@Override
	public void run()
    {
	    while (keepAlive)
        {
		// do nothing
		    try
            {
		      Thread.sleep(100);
		    } catch (InterruptedException e)
              {
		      }
	    }
	}

	public void sendCommand(byte[] bytes)
    {
	    try
        {
		  outStream.write(bytes);

	    } catch (IOException e)
        {
		  e.printStackTrace();
	    }
	}

	public void cancel()
    {
	    keepAlive = false;
	    try
        {
		    btSocket.close();
	    } catch (IOException e)
          {
	      }
	}
    }

    class BTConnectionThread extends Thread
    {
	    private BluetoothDevice device;
	    private BluetoothSocket btSocket;
	    private BTChannelThread btChannelThread;

	public BTConnectionThread(BluetoothDevice device)
    {
	    this.device = device;
	}

	@Override
	public void run()
    {
	    if (bluetoothAdapter.isDiscovering())
        {
		  bluetoothAdapter.cancelDiscovery();
	    }

	    try
        {
		  btSocket = device.createRfcommSocketToServiceRecord(MAGIC_UUID);
		  btChannelThread = new BTChannelThread(btSocket);

		  btSocket.connect();

		if (pd != null)
        {
		    pd.dismiss();
		}

		btChannelThread.start();
	    } catch (IOException e)
        {
		Log.e(TAG, "Can not connect to bluetooth  device" + device.getName(), e);
		try
        {
		    btSocket.close();
		} catch (IOException e1)
        {
		    // supress
		}
	    }

	}

	public void cancel()
    {
	    try
        {
		btSocket.close();
	    } catch (IOException e)
        {
		// supress
	    }
	}

	public void write(byte[] bytes)
    {
	    btChannelThread.sendCommand(bytes);
	}
    }

    @Override
    protected void onDestroy()
    {
	if (btConnectionThread != null)
    {
	    btConnectionThread.cancel();
	}
	super.onDestroy();
    }
}
