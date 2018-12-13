//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework;

import com.inthergroup.webpoc.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author gvandenbekerom
 * @since 09-Oct-18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RegexPatternsTest {
    @Test
    public void testRegexPatternUrl_ShouldMatch() {
        assertTrue("http://foo.com/blah_blah".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/blah_blah/".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/blah_blah_(wikipedia)".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/blah_blah_(wikipedia)_(again)".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://www.example.com/wpstyle/?p=364".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("https://www.example.com/foo/?bar=baz&inga=42".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://142.42.1.1/".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://142.42.1.1:8080/".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/blah_(wikipedia)#cite-1".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/blah_(wikipedia)_blah#cite-1".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/unicode_(✪)_in_parens".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.com/(something)?after=parens".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://j.mp".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("ftp://foo.bar/baz".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://foo.bar/?q=Test%20URL-encoded%20stuff".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://1337.net".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://a.b-c.de".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://223.255.255.254".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://127.0.0.1".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://127.0.0.1:8080".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://localhost".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("http://localhost:8080".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("localhost".matches(Constants.REGEX_PATTERN_URL));
        assertTrue("localhost:50000".matches(Constants.REGEX_PATTERN_URL));
    }

    @Test
    public void testRegexPatternUrl_ShouldNotMatch() {
        assertFalse("asdf".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("asdf:8080".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://asdf".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://asdf:50000".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://.".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://..".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://../".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://?".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://??".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://??/".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://#".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://##".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://##/".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http://foo.bar?q=Spaces should be encoded".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("//".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("//a".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("///a".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("///".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http:///a".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("rdar://1234".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("h://test".matches(Constants.REGEX_PATTERN_URL));
        assertFalse("http:// shouldfail.com".matches(Constants.REGEX_PATTERN_URL));
        assertFalse(":// should fail".matches(Constants.REGEX_PATTERN_URL));
    }
}
