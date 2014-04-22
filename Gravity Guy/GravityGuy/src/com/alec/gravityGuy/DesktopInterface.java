package com.alec.gravityGuy;

public class DesktopInterface implements GoogleInterface{

	@Override
	public void Login() {
		System.out.println("Desktop: would of logged in here");
	}

	@Override
	public void LogOut() {
		System.out.println("Desktop: would of logged out here");
	}

	@Override
	public boolean getSignedIn() {
		System.out.println("Desktop: getSignIn()");
		return false;
	}
	
	public void submitScore(int score){
		System.out.println("Desktop: submitScore: " +score);
	}

	@Override
	public void getScores() {
		System.out.println("Desktop: getScores()");
	}

	@Override
	public void getScoresData() {
		System.out.println("Desktop: getScoresData()");
	}

	@Override
	public void showAds(boolean show) {
		// TODO Auto-generated method stub
		
	}

}
