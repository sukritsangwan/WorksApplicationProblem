package jp.co.wap.exam;

import java.util.List;

import jp.co.wap.exam.lib.Interval;

/**
 * sukritsangwan@gmail.com
 */
public class Problem1 {
	/**
	 * Return the max count of overlapping intervals at any point of time
	 * Register each interval's begin time and end time
	 * Start traversing each minute with count 0 at 00:00 and add 1 whenever any interval starts, and subtract 1 whenever any interval ends
	 * Return the maximum value encountered in the process
	 */
	public int getMaxIntervalOverlapCount(List<Interval> intervals) {
		// no scope of overlap, return 0
		if(null == intervals || intervals.isEmpty())
			return 0;
			
		// myArray has an element for each minute of the day
		// each element in array stores how many intervals are starting or ending at that minute
		int myArray[] = new int[1442];
		
		// go through each interval
		for(Interval interval : intervals) {
		
			// register the start and end in the array
			myArray[interval.getBeginMinuteUnit()]++;
			myArray[interval.getEndMinuteUnit() + 1]--;
		}
		
		int maxOverlap = 0;
		int currOverlap = 0;
		
		// find max overlap
		for(int n : myArray) {
			currOverlap += n;
			if(currOverlap > maxOverlap)
				maxOverlap = currOverlap;
		}
		
		return maxOverlap;
	}
}
