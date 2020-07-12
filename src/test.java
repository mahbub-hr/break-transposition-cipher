import java.util.ArrayList;
import java.util.List;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static String[] decrypt(String str) {

        int getnum = str.length();

        //get possible division number
        List < Integer > val = new ArrayList < Integer > ();
        for (int x = 1; x < getnum; x++) {
            if (getnum % x == 0)
                val.add(x);
        }

        String[] dec = new String[val.size()];
        for (int x = 0; x < val.size(); x++) {
            int now = (int) val.get(x);
            String regex = "(?<=\\G.{" + now + "})";
            String[] get = str.split(regex);

            //tranpose
            char grid[][] = new char[now][get.length];
            for (int y = 0; y < get.length; y++) {
                String nw = get[y];
                for (int z = 0; z < nw.length(); z++) {
                    grid[z][y] = nw.charAt(z);
                }
            }

            //combine
            dec[x] = "";
            for (int y = 0; y < now; y++) {
                for (int z = 0; z < get.length; z++) {
                    dec[x] = dec[x] + grid[y][z];
                }
                dec[x] = dec[x] + " ";
            }

        }


        return dec;
    }

}
