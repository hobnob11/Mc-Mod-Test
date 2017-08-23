package thisisdumb;

import java.math.BigInteger;
import java.util.LinkedList;

public class thisisdumb {

	static double[] BaseResists = { 0.4, // EM
			0.1, // EXP
			0.2, // KIN
			0.35 // THERM
	};
	static double BaseArmour = 486240000; // aka MaxArmour

	public static void main(String[] args) {
		double startTime = System.currentTimeMillis();
		System.out.println("GENERATING MODS");
		LinkedList<CoreMod> Nodes = GenerateNewNodes();
		System.out.println("THAT TOOK: " + (System.currentTimeMillis() - startTime) + "MS");
		System.out.println("BRACE FOR CALCEHP");
		
		//CoreMod[] goldenMods;
		
		double bestEHP = 0;
		CoreMod goldenMod = new CoreMod(0,0,0,0);

		for (CoreMod mod : Nodes) {
			double curEHP = CalcEHP(1, new CoreMod[] { mod });
			if (curEHP > bestEHP) {
				goldenMod = mod;
				bestEHP = curEHP;
				System.out.printf("\nEHP: %.2f | EM: %d | EXP: %d | KIN: %d | THERM: %d", curEHP, goldenMod.EM, goldenMod.EXP, goldenMod.KIN, goldenMod.THERM);
			}
		}
		
		double[] resists = CalcResists(1, new CoreMod[]{goldenMod});
		System.out.printf("\nResists: %.2f, %.2f, %.2f, %.2f",resists[0],resists[1],resists[2],resists[3]);
		// CoreMod testMod = new CoreMod(60,60,60,60);
		// CoreMod testMod2 = new CoreMod(60,60,30,30);

		// System.out.printf("\n%.2f\n" , CalcEHP(2,new
		// CoreMod[]{testMod,testMod2}));

		System.out.println("Total Time Taken: " + (System.currentTimeMillis() - startTime));
	}

	public static LinkedList<CoreMod> GenerateNewNodes() {
		LinkedList<CoreMod> Nodes = new LinkedList<CoreMod>();
		for (int EM = -20; EM <= 60; EM++) {
			for (int EXP = -20; EXP <= 60; EXP++) {
				for (int KIN = -20; KIN <= 60; KIN++) {
					for (int THERM = -20; THERM <= 60; THERM++) {
						CoreMod mod = new CoreMod(EM, EXP, KIN, THERM);
						if (mod.isValid()) {
							Nodes.add(mod);
						}
					}
				}
			}
		}
		return Nodes;
	}

	public static double CalcEHP(int modCount, CoreMod[] mods) {
		double EHP = BaseArmour;

		int[] rEM = new int[modCount];
		int[] rEXP = new int[modCount];
		int[] rKIN = new int[modCount];
		int[] rTHERM = new int[modCount];

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

		return EHP / (1 - (EM + EXP + KIN + THERM) / 4);
	}

	public static double[] CalcResists(int modCount, CoreMod[] mods) {

		int[] rEM = new int[modCount];
		int[] rEXP = new int[modCount];
		int[] rKIN = new int[modCount];
		int[] rTHERM = new int[modCount];

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

	public static double CalcResist(double startResistance, int[] resists) {
		double resistance = startResistance * 100.0;

		for (int i = 0; i < resists.length; i++) {
			double resist = resists[i];
			resistance = resistance + (resist * (0.5 / Math.pow(i + 1, 1.2)));
		}
		// System.out.printf("%.2f | ",resistance);
		return Math.max(-90.0, Math.min(90.0, resistance)) / 100.0;
	}

	public static double CalcEHP(int[][] coreMods) {
		double EHP = BaseArmour;
		double[] resists = new double[4];

		for (int type = 0; type < 4; type++) {
			resists[type] = CalcResist(BaseResists[type], coreMods[type]);
		}
		EHP = EHP / (1 - (resists[0] + resists[1] + resists[2] + resists[3]) / 4);
		return EHP;
	}

}
