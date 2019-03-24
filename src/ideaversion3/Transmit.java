package ideaversion3;


public class Transmit implements IDEAinter {
	private static final int rounds = 8;
	int[] encSubKey;
        int[] decSubKey;
		
	public Transmit(byte[] key){
		encSubKey = setEncSubKeys(key);
		decSubKey = setDecSubKeys(encSubKey);
                 System.out.println("\n subkeys w :");
                for(int a=0;a<encSubKey.length;a++)
                {
                    System.out.print(encSubKey[a]+"-");
                }
                System.out.print("\n");
	}
	
	public void encrypt(byte[] data, int i){
		crypt(data, i, encSubKey);
                
	}
	
	public void decrypt(byte[] data, int i){
		crypt(data, i, decSubKey);	
	}

	private void crypt(byte[] data, int i, int[] subKey){
           
	   int x0 = ((data[i+0] & 0xFF) << 8) | (data[i+1] & 0xFF);
	   int x1 = ((data[i+2] & 0xFF) << 8) | (data[i+3] & 0xFF);
	   int x2 = ((data[i+4] & 0xFF) << 8) | (data[i+5] & 0xFF);
	   int x3 = ((data[i+6] & 0xFF) << 8) | (data[i+7] & 0xFF);
	   //
	   int p = 0;
	   for (int round = 0; round < rounds; round++) 
	   {
	      int y0 = mul(x0, subKey[p++]);
	      int y1 = add(x1, subKey[p++]);
	      int y2 = add(x2, subKey[p++]);
	      int y3 = mul(x3, subKey[p++]);
	      //
	      int t0 = mul(y0 ^ y2, subKey[p++]);
	      int t1 = add(y1 ^ y3, t0);
	      int t2 = mul(t1, subKey[p++]);
	      int t3 = add(t0, t2);
	      //
	      x0 = xor(y0,t2);
	      x1 = xor(y2,t2);
	      x2 = xor(y1,t3);
	      x3 = xor(y3,t3); 
	    
	   }
	   //
	   int r0 = mul(x0, subKey[p++]);
	   int r1 = add(x2, subKey[p++]);
	   int r2 = add(x1, subKey[p++]);
	   int r3 = mul(x3, subKey[p++]);
	   //
	   data[i+0] = (byte)(r0 >> 8);
	   data[i+1] = (byte)r0;
	   data[i+2] = (byte)(r1 >> 8);
	   data[i+3] = (byte)r1;
	   data[i+4] = (byte)(r2 >> 8);
	   data[i+5] = (byte)r2;
	   data[i+6] = (byte)(r3 >> 8);
	   data[i+7] = (byte)r3;
	}
		
	private static int add(int a, int b) {
		return (a + b) & 0xFFFF;
	}
	
	private static int mul(int a, int b) {
	   long r = (long) a * (long) b;
	   if (r != 0)
	      return (int)(r % 0x10001) & 0xFFFF; 
	    else
	      return (1 - a - b) & 0xFFFF; 
	}
	
	private static int xor(int a, int b) {
		return a ^ b;
	}
	
	// Get subkeys for encryption
	private static int[] setEncSubKeys(byte[] key) {
	   if (key.length != 16)
	      throw new IllegalArgumentException(); 

	   int[] subKey = new int[rounds * 6 + 4];
	   
	   for (int i = 0; i < key.length / 2; i++)
	      subKey[i] = ((key[2 * i] & 0xFF) << 8) | (key[2 * i + 1] & 0xFF);
	   
	   for (int i = key.length / 2; i < subKey.length; i++)
	      subKey[i] = ((subKey[(i + 1) % 8 != 0 ? i - 7 : i - 15] << 9) | (subKey[(i + 2) % 8 < 2 ? i - 14 : i - 6] >> 7)) & 0xFFFF;
	   
	   return subKey;
	}
	
	// Get subkeys for decryption
	private static int[] setDecSubKeys(int[] key) {
	   int[] invKey = new int[key.length];
	   int p = 0;
	   int i = rounds * 6;
	   invKey[i + 0] = mulInv(key[p++]);
	   invKey[i + 1] = addInv(key[p++]);
	   invKey[i + 2] = addInv(key[p++]);
	   invKey[i + 3] = mulInv(key[p++]);
	   for (int r = rounds - 1; r >= 0; r--) {
	      i = r * 6;
	      int m = r > 0 ? 2 : 1;
	      int n = r > 0 ? 1 : 2;
	      invKey[i + 4] =        key[p++];
	      invKey[i + 5] =        key[p++];
	      invKey[i + 0] = mulInv(key[p++]);
	      invKey[i + m] = addInv(key[p++]);
	      invKey[i + n] = addInv(key[p++]);
	      invKey[i + 3] = mulInv(key[p++]); 
	   }
	   return invKey; 
	}

	// Additive Inverse.
	private static int addInv (int x) {
	   return (0x10000 - x) & 0xFFFF; 
	}

	// Multiplicative inverse.
	private static int mulInv (int x) {
	   if (x <= 1) {
	      return x; 
	   }
	   int y = 0x10001;
	   int t0 = 1;
	   int t1 = 0;
	   while (true) {
	      t1 += y / x * t0;
	      y %= x;
	      if (y == 1) {
	         return 0x10001 - t1; 
	      }
	      t0 += x / y * t1;
	      x %= y;
	      if (x == 1) {
	         return t0; 
	      }
	   }
	}
}
