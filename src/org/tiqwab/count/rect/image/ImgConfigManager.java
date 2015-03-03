package org.tiqwab.count.rect.image;

public class ImgConfigManager {

	private static double HOUGHLINES_RHO = 1;
	private static double HOUGHLINES_THETA = Math.PI/180;
	private static int HOUGHLINES_THRESHOLD = 60;
	private static double HOUGHLINES_LENGTH = 60;
	private static double HOUGHLINES_GAP = 10;
	
	
	public static class HoughConfig {		
		public HoughConfig() {}		
		public double getRho() { return HOUGHLINES_RHO; }
		public double getTheta() { return HOUGHLINES_THETA; }
		public int getThreshold() { return HOUGHLINES_THRESHOLD; }
		public double getLength() { return HOUGHLINES_LENGTH; }
		public double getGap() { return HOUGHLINES_GAP; }
		
		public HoughConfig setRho(double val) {
			HOUGHLINES_RHO = val;
			return this;
		}
		public HoughConfig setTheta(double val) {
			HOUGHLINES_THETA = val;
			return this;
		}
		public HoughConfig setThreshold(int val) {
			HOUGHLINES_THRESHOLD = val;
			return this;
		}
		public HoughConfig setLength(double val) {
			HOUGHLINES_LENGTH = val;
			return this;
		}
		public HoughConfig setGap(double val) {
			HOUGHLINES_GAP = val;
			return this;
		}
		
		public void hl_setLow() {
			setRho(1).setTheta(Math.PI / 180).setThreshold(100).setLength(100).setGap(10);
		}
				
		public void hl_setMid() {
			setRho(1).setTheta(Math.PI / 180).setThreshold(60).setLength(60).setGap(10);
		}		
		
		public void hl_setHigh() {
			setRho(1).setTheta(Math.PI / 180).setThreshold(30).setLength(30).setGap(10);
		}
	}
}
