package de.cutl.chris.project.launcher;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LauncherCtrTest {

    LauncherCtr cut;
    
    @BeforeMethod
    public void setUp() {
        cut = new LauncherCtr();
    }
    
    @Test
    public void getCmdPartSimple() {
        Assert.assertEquals(cut.removeArguments("test"), "test");
    }
    
    @Test
    public void getCmdPartWithArguments() {
        Assert.assertEquals(cut.removeArguments("test -e -c -d -e -f -g"), "test");
    }
}
