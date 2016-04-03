package com.geno;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CubingChinaAreaGetter
{
    public static void main(String[] args) throws IOException
    {
		long t0 = System.currentTimeMillis();
		File f = new File(args[0]);
		BufferedReader br;
		File fToWrite = new File(args[0] + "s");
		BufferedWriter bw;
		File db = new File(args[0] + ".db");
		BufferedReader br2;
		Map<String, String> addr = new HashMap<>();
		int i = 0;
		int j = 0;
		try
		{
			br = new BufferedReader(new FileReader(f));
			bw = new BufferedWriter(new FileWriter(fToWrite));
			if (db.exists())
			{
				br2 = new BufferedReader(new FileReader(db));
				JSONObject o = JSONObject.parseObject(br2.readLine());
				addr = (Map) o;
				br2.close();
			}
			String t;
			while ((t = br.readLine()) != null)
			{
				String wcaId = t.split("\t")[7];
				String address;
				if (!addr.containsKey(wcaId))
				{
					j++;
					Document d = Jsoup.connect("http://cubingchina.com/results/person/" + wcaId).get();
					Element e = d.select("span.info-value").get(1);
					System.out.println("Get count: " + j);
					addr.put(wcaId, e.text());
				}
				address = addr.get(wcaId);
				System.out.println(i + " " + address);
				bw.write(t + "\t" + address + "\n");
				i++;
			}
			br.close();
			bw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JSONObject o = new JSONObject();
			o.putAll(addr);
			BufferedWriter bwb = new BufferedWriter(new FileWriter(db));
			bwb.write(o.toString());
			bwb.close();
		}
		JSONObject o = new JSONObject();
		o.putAll(addr);
		BufferedWriter bwb = new BufferedWriter(new FileWriter(db));
		bwb.write(o.toString());
		bwb.close();
		long t1 = System.currentTimeMillis();
		System.out.println(t1 - t0);
	}
}