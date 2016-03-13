package com.carlosgracite.redroid;

import com.carlosgracite.redroid.processor.StringUtils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void emptyStringShouldBeTrue() {
        Assert.assertTrue(StringUtils.isEmpty(""));
        Assert.assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    public void nonEmptyStringsShouldBeFalse() {
        Assert.assertFalse(StringUtils.isEmpty("asd"));
    }

}
