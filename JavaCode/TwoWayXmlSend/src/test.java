
public class test {

	public static void main(String[] args) throws Exception {
		
		
		EncryptDecrypt ende = new EncryptDecrypt();
		String cleartextFile = "cleartext.xml";
		String ciphertextFile = "ciphertextRSA.xml";
		
		ende.encrypt("mypublickey.key",cleartextFile,ciphertextFile);
		 
		
		
		String cleartextAgainFile = "cleartextAgainRSA.xml";
		
		ende.decrypt(ciphertextFile,cleartextAgainFile);
	}
}
