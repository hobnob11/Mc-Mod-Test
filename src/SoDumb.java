

import java.util.Arrays;
import java.util.Collections;

public class SoDumb {

	public static double[] BaseResists = { 0.4, // EM
			0.1, // EXP
			0.2, // KIN
			0.35 // THERM
	};
	public static double BaseArmour = 1000; // aka MaxArmour

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
