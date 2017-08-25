import java.util.concurrent.Callable;

public class Competence implements Callable<Incompetence> {

	public int EM;
	public int endEM;
	public int depth;
	public CoreMod[] goldenMods = new CoreMod[10];
	public String name;
	
	public Competence(String name, int EM, int depth, CoreMod[] goldenMods) {
		this.name = name;
		this.EM = EM;
		this.depth = depth;
		this.goldenMods = goldenMods;
	}

	@Override
	public Incompetence call() {
		double bestEHP = 0.0;
		CoreMod goldenMod = new CoreMod(0, 0, 0, 0, 0);
		for (int EXP = -20; EXP <= 60; EXP++) {
			for (int KIN = -20; KIN <= 60; KIN++) {
				for (int THERM = -20; THERM <= 60; THERM++) {
					for (int ARMOURP = -20; ARMOURP <= 60; ARMOURP++) {
						CoreMod mod = new CoreMod(EM, EXP, KIN, THERM, ARMOURP);
						if (mod.isValid()) {
							if (depth == 1) {
								double curEHP = SoDumb.CalcEHP(1, new CoreMod[] { mod });
								if (curEHP > bestEHP) {
									goldenMod = mod;
									bestEHP = curEHP;
								}
							} else {
								double curEHP = SoDumb.CalcEHP(1, goldenMods, mod);
								if (curEHP > bestEHP) {
									goldenMod = mod;
									bestEHP = curEHP;
								}
							}
						}
					}
				}
			}
		}
		//System.out.println(name + " Finished! returning output!");
		return new Incompetence(goldenMod, bestEHP);
	}

}

class Incompetence {
	public CoreMod mod;
	public double EHP;

	Incompetence(CoreMod mod, double EHP) {
		this.mod = mod;
		this.EHP = EHP;
	}
}
