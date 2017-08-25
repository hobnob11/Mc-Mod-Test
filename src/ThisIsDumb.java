
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThisIsDumb
{

	public static void main(String[] args)
	{

		long startTime = System.currentTimeMillis();
		long timeDelta = 0;
		CoreMod[] goldenMods = new CoreMod[10];
		goldenMods[0] = new CoreMod(0, 0, 0, 0, 0);

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		
		for(int depth = 1; depth <= 10; depth++)
		{	
			double bestEHP = 0;
			CoreMod goldenMod = new CoreMod(0, 0, 0, 0, 0);
			System.out.println("Starting Calculations for depth: " + depth);
			List<Future<Incompetence>> cadets = new ArrayList<Future<Incompetence>>();
			System.out.println("Generating Threads: ");
			for (int EM = -20; EM <= 60; EM++)
			{
				Future<Incompetence> njits = executor.submit(new Competence("Competence Thread[EM: " + EM+"]", EM, 1, goldenMods));
				cadets.add(njits);
	
				System.out.print("|");
			}
			System.out.println("\nDone, Calculating");
			for (Future<Incompetence> njits : cadets)
			{
				try
				{
					Incompetence pinball = njits.get();
					if (pinball.EHP > bestEHP)
					{
						goldenMod = pinball.mod;
						bestEHP = pinball.EHP;
					}
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			goldenMods[depth-1] = goldenMod;
			
			timeDelta = System.currentTimeMillis() - startTime;
			System.out.println("Depth: " + depth + " Completed.");
			for(CoreMod mod : goldenMods)
			{
				System.out.println(mod);
			}
			double[] resists = SoDumb.CalcResists(1, goldenMods);
			System.out.printf("\nResists: %.2f, %.2f, %.2f, %.2f\n", resists[0], resists[1], resists[2], resists[3]);
			System.out.println("EHP: " + bestEHP + ", Took: " + (new SimpleDateFormat("mm:ss:SSS")).format(new Date(timeDelta)));
		}
	}
}
