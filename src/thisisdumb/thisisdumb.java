package thisisdumb;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

public class thisisdumb {

	static double[] BaseResists = { 0.4, // EM
			0.1, // EXP
			0.2, // KIN
			0.35 // THERM
	};
	static double BaseArmour = 1000; // aka MaxArmour

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println("GENERATING MODS");
		LinkedList<CoreMod> Nodes = GenerateNewNodes();

		long nodetime = System.currentTimeMillis() - startTime;
		System.out.println("THAT TOOK: " + (new SimpleDateFormat("mm:ss:SSS")).format(new Date(nodetime)));

		System.out.println("BRACE FOR CALCEHP");

		CoreMod[] goldenMods = new CoreMod[10];
		goldenMods[0] = new CoreMod(0, 0, 0, 0);

		for (int depth = 1; depth <= 10; depth++) {
			System.out.println("=============DEPTH: " + depth + "====================");
			double bestEHP = 0;
			for (CoreMod mod : Nodes) {
				if (depth == 1) {
					double curEHP = CalcEHP(1, new CoreMod[] { mod });
					if (curEHP > bestEHP) {
						goldenMods[0] = mod;
						bestEHP = curEHP;
						// System.out.printf("\nEHP: %.2f | EM: %d | EXP: %d |
						// KIN: %d | THERM: %d", curEHP, goldenMods[0].EM,
						// goldenMods[0].EXP, goldenMods[0].KIN,
						// goldenMods[0].THERM);
					}
				} else {
					double curEHP = CalcEHP(depth, goldenMods, mod);
					if (curEHP > bestEHP) {
						goldenMods[depth - 1] = mod;
						bestEHP = curEHP;
						// System.out.printf("\nEHP: %.2f | EM: %d | EXP: %d |
						// KIN: %d | THERM: %d", curEHP, goldenMods[depth-1].EM,
						// goldenMods[depth-1].EXP, goldenMods[depth-1].KIN,
						// goldenMods[depth-1].THERM);
					}
				}
			}
		}
		double[] resists = CalcResists(10, goldenMods);
		for (CoreMod mod : goldenMods) {
			System.out.println(mod);
		}
		System.out.printf("\nEHP:%.2f", CalcEHP(10, goldenMods));
		System.out.printf("\nResists: %.2f, %.2f, %.2f, %.2f", resists[0], resists[1], resists[2], resists[3]);
		// CoreMod testMod = new CoreMod(60,60,60,60);
		// CoreMod testMod2 = new CoreMod(60,60,30,30);

		// System.out.printf("\n%.2f\n" , CalcEHP(2,new
		// CoreMod[]{testMod,testMod2}));

		long totaltime = System.currentTimeMillis() - startTime;
		System.out.println("Total Time Taken: " + (new SimpleDateFormat("mm:ss:SSS")).format(new Date(totaltime)));
	}

	public static LinkedList<CoreMod> GenerateNewNodes() {
		LinkedList<CoreMod> Nodes = new LinkedList<CoreMod>();
		float progress = 0.0f;
		long modtimestart = System.currentTimeMillis();
		for (int EM = -20; EM <= 60; EM++) {
			progress = ((float) EM + 20f) / 80f;
			long timesincelast = System.currentTimeMillis() - modtimestart;
			System.out.println("MOD PROGRESS: " + (progress * 100) + "%, "
					+ (new SimpleDateFormat("mm:ss:SSS")).format(new Date(timesincelast)));
			for (int EXP = -20; EXP <= 60; EXP++) {
				for (int KIN = -20; KIN <= 60; KIN++) {
					for (int THERM = -20; THERM <= 60; THERM++) {
						for (int ARMOURP = -20; ARMOURP <= 60; ARMOURP++) {
							CoreMod mod = new CoreMod(EM, EXP, KIN, THERM, ARMOURP);
							if (mod.isValid()) {
								Nodes.add(mod);
							}
						}
					}
				}
			}
		}
		return Nodes;
	}

	public static double CalcEHP(int modCount, CoreMod[] mods) {
		double EHP = BaseArmour;

		Integer[] rEM = new Integer[modCount];
		Integer[] rEXP = new Integer[modCount];
		Integer[] rKIN = new Integer[modCount];
		Integer[] rTHERM = new Integer[modCount];
		Integer[] rARMOURP = new Integer[modCount];

		for (int i = 0; i < modCount; i++) {
			rEM[i] = mods[i].EM;
			rEXP[i] = mods[i].EXP;
			rKIN[i] = mods[i].KIN;
			rTHERM[i] = mods[i].THERM;
			rARMOURP[i] = mods[i].ARMOURP;
		}
		double EM = CalcResist(BaseResists[0], rEM);
		double EXP = CalcResist(BaseResists[1], rEXP);
		double KIN = CalcResist(BaseResists[1], rKIN);
		double THERM = CalcResist(BaseResists[1], rTHERM);
		double ArmourP = 0.0;
		for (int i = 1; i <= modCount; i++) {
			ArmourP = ArmourP + ((rARMOURP[i - 1] / 100 * 1) * (1 / Math.pow(i, 1.45)));
		}
		EHP = EHP + (EHP * ArmourP);

		return EHP / (1 - (EM + EXP + KIN + THERM) / 4);
	}

	public static double CalcEHP(int modCount, CoreMod[] mods, CoreMod lastMod) {
		double EHP = BaseArmour;

		Integer[] rEM = new Integer[modCount];
		Integer[] rEXP = new Integer[modCount];
		Integer[] rKIN = new Integer[modCount];
		Integer[] rTHERM = new Integer[modCount];
		Integer[] rARMOURP = new Integer[modCount];

		for (int i = 0; i < modCount - 1; i++) {
			rEM[i] = mods[i].EM;
			rEXP[i] = mods[i].EXP;
			rKIN[i] = mods[i].KIN;
			rTHERM[i] = mods[i].THERM;
			rARMOURP[i] = mods[i].ARMOURP;
		}

		rEM[modCount - 1] = lastMod.EM;
		rEXP[modCount - 1] = lastMod.EXP;
		rKIN[modCount - 1] = lastMod.KIN;
		rTHERM[modCount - 1] = lastMod.THERM;
		rARMOURP[modCount - 1] = lastMod.ARMOURP;

		double EM = CalcResist(BaseResists[0], rEM);
		double EXP = CalcResist(BaseResists[1], rEXP);
		double KIN = CalcResist(BaseResists[1], rKIN);
		double THERM = CalcResist(BaseResists[1], rTHERM);
		double ArmourP = 0.0;
		for (int i = 1; i <= modCount; i++) {
			ArmourP = ArmourP + ((rARMOURP[i - 1] / 100 * 1) * (1 / Math.pow(i, 1.45)));
		}
		EHP = EHP + (EHP * ArmourP);

		return EHP / (1 - (EM + EXP + KIN + THERM) / 4);
	}

	public static double[] CalcResists(int modCount, CoreMod[] mods) {

		Integer[] rEM = new Integer[modCount];
		Integer[] rEXP = new Integer[modCount];
		Integer[] rKIN = new Integer[modCount];
		Integer[] rTHERM = new Integer[modCount];

		for (int i = 0; i < modCount; i++) {
			rEM[i] = mods[i].EM;
			rEXP[i] = mods[i].EXP;
			rKIN[i] = mods[i].KIN;
			rTHERM[i] = mods[i].THERM;
		}
		double EM = CalcResist(BaseResists[0], rEM);
		double EXP = CalcResist(BaseResists[1], rEXP);
		double KIN = CalcResist(BaseResists[1], rKIN);
		double THERM = CalcResist(BaseResists[1], rTHERM);

		return new double[] { EM, EXP, KIN, THERM };
	}

	public static double CalcResist(double startResistance, Integer[] resists) {
		double resistance = startResistance * 100.0;

		Arrays.sort(resists, Collections.reverseOrder());

		for (int i = 0; i < resists.length; i++) {
			double resist = resists[i];
			resistance = resistance + (resist * (0.5 / Math.pow(i + 1, 1.2)));
		}
		// System.out.printf("%.2f | ",resistance);
		return Math.max(-90.0, Math.min(90.0, resistance)) / 100.0;
	}

	public static double CalcEHP(Integer[][] coreMods) {
		double EHP = BaseArmour;
		double[] resists = new double[4];

		for (int type = 0; type < 4; type++) {
			resists[type] = CalcResist(BaseResists[type], coreMods[type]);
		}
		EHP = EHP / (1 - (resists[0] + resists[1] + resists[2] + resists[3]) / 4);
		return EHP;
	}

}
