package team68.chatbots.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import android.os.Environment;

public class SendImage extends Thread {
	
	private String filename;
	private SocketChannel socketChannel;
	private String ipAddress;
	public SendImage(String filen, String ipadd){
		this.filename = filen;
		this.ipAddress = ipadd;
	}
	
	public void run(){
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress(ipAddress, 6969));
			
			// send filename, filesize
			
			long fileSize = 0;
			String PATH = Environment.getExternalStorageDirectory() + "/DCIM/" + filename;
			File dir = new File(PATH);
			fileSize = dir.length();
			String buf = filename + "|" + fileSize + "|";
			socketChannel.write(ByteBuffer.wrap(buf.getBytes()));
			
			RandomAccessFile aFile     = new RandomAccessFile(PATH, "rw");
			// send file
			while (fileSize > 0) {
				//String d = System.getProperty("user.dir");
				
				byte[] bb = new byte[4096];
				int le = aFile.read(bb);
				if (le > 0) {
					socketChannel.write(ByteBuffer.wrap(bb, 0, le));
				}
				fileSize -= le;
			}
			aFile.close();
			ByteBuffer bufer = ByteBuffer.allocate(1024);
			int numRead = socketChannel.read(bufer);
			String idImageResponse = new String(bufer.array(), 0, numRead);
			
			socketChannel.finishConnect();
			//makeToast(idImageResponse);
		} catch (IOException e) {
			e.printStackTrace();
			//makeToast("Server Disconnect");
			
		}
	}

}
