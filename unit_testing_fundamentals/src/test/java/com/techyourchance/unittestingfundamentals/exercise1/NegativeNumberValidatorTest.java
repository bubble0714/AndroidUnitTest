package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void set(){
        SUT = new NegativeNumberValidator();
    }

    //If number is greater than 0.
    @Test
    public void negative_numberValidator_nagetiveReturned(){
        boolean result = SUT.isNegative(-1);
        Assert.assertThat(result, is(true));
    }

    //If number is equal to 0.
    @Test
    public void negative_numberValidator_equalZeroReturned(){
        boolean result = SUT.isNegative(0);
        Assert.assertThat(result, is(false));
    }

    //If number is less than 0.
    @Test
    public void negative_numberValidator_positiveReturned(){
        boolean result = SUT.isNegative(1);
        Assert.assertThat(result, is(false));
    }
}