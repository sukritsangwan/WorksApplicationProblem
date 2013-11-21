package jp.co.wap.exam;

import java.util.*;
import jp.co.wap.exam.lib.Interval;

/**
 * sukritsangwan@gmail.com
 */
public class Problem2 {
	/**
	 * Return the max amount of time a worker can devote to different tasks
	 * Sort intervals by begin time
	 * For each interval find the next possible task
	 * Now start traversing the intervals from back
	 * Get the max amount of work possible at each interval while traversing
	 * Return the max (at first interval)
	 */
	public int getMaxWorkingTime(List<Interval> intervals) {
		// no scope of overlap, return 0
		if(null == intervals || 0 == intervals.size())
			return 0;
			
		int size = intervals.size();
		// number of intervals can't be more than 10000
        if(size > 10000 )
            throw new IllegalArgumentException();
           
		// sort intervals by begin time
		Collections.sort(intervals, new IntervalBeginCompare());
		
		// for each interval store the next possible interval
		// get only the next possible first interval, as all other intervals starting after that will also be eligible
		int[] nextTask = new int[size];
		
		// while traversing the intervals from behind,
		// we will store the max amount of work possible after any interval in this array
		// so at last we will return its first element
		int[] maxTimeList = new int[size + 1];
		
		// for each interval get the next possible interval
		// do binary search, as the intervals are sorted
		for(int i = 0; i < size; i++) {
			int begin = intervals.get(i).getBeginMinuteUnit();
			int end = intervals.get(i).getEndMinuteUnit();
			
			// if this interval spans whole day, its definitely maximum, return it
			if(1440 == end - begin)
				return 1440;
			
			// in case we can't find any possible interval after this interval,
			// initialize the next interval index to "size"
			nextTask[i] = size;
			
			// minIndex and maxIndex for binary search
			// j is the middle index
			// we have to find the next first possible task that can be
			// performed after the completion of this task
			int minI = i + 1;
			int maxI = size - 1;
			int j = (minI + maxI) / 2;
			
			// continue only whenever j is within limits, and minIndex <= maxIndex
			while(j > i && j < size && minI <= maxI) {
				j = (minI + maxI) / 2;
				
				// the next possible task begins after the completion of this task
				if(intervals.get(j).getBeginMinuteUnit() > end) {
				
					// since we have to find the next "first" possible task
					// check if previous task begins before completion of ith interval
					if(intervals.get(j - 1).getBeginMinuteUnit() <= end) {
						nextTask[i] = j;
						break;
					}
					// if not then we should decrease the maxIndex
					else
						maxI = j - 1;
				}
				// if jth task starts before completion of ith task,
				// increase the minIndex
				else
					minI = j + 1;
			}
			
			// store the amount of work this task alone can contribute
			maxTimeList[i] = end - begin;
		}
		
		// traverse the intervals list(sorted by starting time) from behind
		// at each interval, note down the max work possible after starting point of that interval
		// that must be equal to the maximum value amongst
		// (this interval + next possible interval), or equal to next element in the array
		// think of this by induction, start from last
		for(int i = size - 2; i >= 0; i--) {
			int temp = intervals.get(i).getIntervalMinute() + maxTimeList[nextTask[i]];
			if(temp < maxTimeList[i + 1])
				maxTimeList[i] = maxTimeList[i + 1];
			else
				maxTimeList[i] = temp;
		}
		
		// since the maxTimeList array denotes the max work possible after starting point of each interval
		// return the first element of array, as it is the max work possible after start time of 1st task
		return maxTimeList[0];
	}
}

// comparator to sort intervals by begin time
class IntervalBeginCompare implements Comparator<Interval> {
	@Override
	public int compare(Interval i1, Interval i2) {
		if(i1.getBeginHour() > i2.getBeginHour())
			return 1;
		else if(i1.getBeginHour() < i2.getBeginHour())
			return -1;
		else if(i1.getBeginMinute() > i2.getBeginMinute())
			return 1;
		else
			return -1;
	}
}
