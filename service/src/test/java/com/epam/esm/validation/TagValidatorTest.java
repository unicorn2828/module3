package com.epam.esm.validation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceException;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.gen5.api.Assertions.assertTrue;

class TagValidatorTest {
    private static final String TEST = "test";
    private TagDto test;

    @BeforeEach
    void setUp() {
        test = new TagDto();
        test.setName(TEST);
    }

    @AfterEach
    public void tierDown() {
        test = null;
    }

    @Test
    void isTag() {
        boolean actual = TagValidator.isTag(test);
        assertTrue(actual);
    }

    @Test
    void isTagNegative() {
        test.setName(null);
        Assertions.assertThrows(ServiceException.class, () -> {
            TagValidator.isTag(test);
        });
    }

    @Test
    void isTagNegativeNull() {
        test = null;
        Assertions.assertThrows(ServiceException.class, () -> {
            TagValidator.isTag(test);
        });
    }
}
