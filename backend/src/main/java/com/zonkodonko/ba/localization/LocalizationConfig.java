package com.zonkodonko.ba.localization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Factory to create beans that are needed to resolve current languages.
 *
 * @author Timm
 * @version 11.09.2025
 */
@Configuration
public class LocalizationConfig {

	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
		slr.setDefaultLocale(Locale.ENGLISH);
		return slr;
	}
}
