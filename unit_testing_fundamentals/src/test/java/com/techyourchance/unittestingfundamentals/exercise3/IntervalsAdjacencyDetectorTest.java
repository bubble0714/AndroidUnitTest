package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }

    @Test
    public void isAdjacent_Inverval1BeforeInterval2_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(6,10);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_Inverval1BeforeAndAdjectToInterval2_trueReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(5,10);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_Inverval1OverlapInterval2OnStart_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(3,10);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_Inverval1EqualInterval2_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_Inverval1WithinInterval2_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(-3,8);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_Inverval2WithinInterval1_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(1,3);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_Inverval2OverlapInterval1OnStart_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(-3,3);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_Inverval2AdjecentInterval1OnStart_trueReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(-3,0);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_Inverval1AfterInterval2End_falseReturned() throws Exception{
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(-5,-1);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }
}