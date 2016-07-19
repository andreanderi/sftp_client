package example.andre;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClient {

	private final static Logger LOG = LoggerFactory.getLogger(SftpClient.class);

	private String targetFile;
	
	private String  user     = "andre";
	private String  password = "password";
	private String  host     = "localhost";
	private Integer port     = 22;

	public SftpClient(String tarPath) {
		targetFile = tarPath;
	}

	public boolean sendFileToSftp() {

		LOG.info("Send a file through sftp");
		
			Properties config = new Properties();
			String fileName = "test.txt";

			try {
				JSch ssh = new JSch();
				Session session = ssh.getSession(user, host, port);
				session.setPassword(password);
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();

				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;

				sftp.put( targetFile, fileName);

				LOG.info("The file " + fileName + " has been sent to the sftp server.");
				
				channel.disconnect();
				session.disconnect();
			} catch (JSchException e) {
				LOG.error("could not connect to the sftp server",e);
				e.printStackTrace();
				return false;
			} catch (SftpException e) {
				LOG.error("could not save the file in the sftp server",e);
				e.printStackTrace();
				return false;
			}	
		return true;
	}
}
