package org.molgenis.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.molgenis.framework.server.MolgenisSettings;
import org.molgenis.framework.ui.MolgenisPluginController;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MolgenisPluginInterceptorTest
{
	private MolgenisUi molgenisUi;
	private Authentication authentication;

	@BeforeMethod
	public void setUp()
	{
		molgenisUi = mock(MolgenisUi.class);
		authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn("username");
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void MolgenisPluginInterceptor()
	{
		new MolgenisPluginInterceptor(null, null);
	}

	@Test
	public void preHandle() throws Exception
	{
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "test";
		MolgenisPluginController molgenisPlugin = new MolgenisPluginController(uri)
		{
		};
		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(molgenisPlugin);

		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);

		MockHttpServletRequest request = new MockHttpServletRequest();
		assertTrue(molgenisPluginInterceptor.preHandle(request, null, handlerMethod));
		assertEquals(request.getAttribute(MolgenisPluginAttributes.KEY_CONTEXT_URL), uri);
	}

	@Test
	public void preHandle_hasContextUrl() throws Exception
	{
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "test";
		MolgenisPluginController molgenisPlugin = new MolgenisPluginController(uri)
		{
		};
		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(molgenisPlugin);

		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);

		String contextUri = "/plugin/not-test";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(MolgenisPluginAttributes.KEY_CONTEXT_URL, contextUri);
		assertTrue(molgenisPluginInterceptor.preHandle(request, null, handlerMethod));
		assertEquals(request.getAttribute(MolgenisPluginAttributes.KEY_CONTEXT_URL), contextUri);

	}

	@Test
	public void postHandle() throws Exception
	{
		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "test";
		ModelAndView modelAndView = new ModelAndView();
		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(new MolgenisPluginController(uri)
		{
		});
		molgenisPluginInterceptor.postHandle(null, null, handlerMethod, modelAndView);
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_PLUGIN_ID), "test");
		assertNotNull(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_MOLGENIS_UI));
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_AUTHENTICATED), false);
		assertEquals(modelAndView.getModel().get("footerText"), "footerTest");
	}

	@Test
	public void postHandle_pluginIdExists() throws Exception
	{
		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "test";
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(MolgenisPluginAttributes.KEY_PLUGIN_ID, "plugin_id");
		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(new MolgenisPluginController(uri)
		{
		});
		molgenisPluginInterceptor.postHandle(null, null, handlerMethod, modelAndView);
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_PLUGIN_ID), "plugin_id");
		assertNotNull(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_MOLGENIS_UI));
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_AUTHENTICATED), false);
		assertEquals(modelAndView.getModel().get("footerText"), "footerTest");
	}

	@Test
	public void postHandlePluginidWithQueryString() throws Exception
	{
		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "plugin_id_test";

		HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
		when(mockHttpServletRequest.getQueryString()).thenReturn("entity=entityName");

		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(new MolgenisPluginController(uri)
		{
		});

		ModelAndView modelAndView = new ModelAndView();
		molgenisPluginInterceptor.postHandle(mockHttpServletRequest, null, handlerMethod, modelAndView);
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_PLUGIN_ID), "plugin_id_test");
		assertNotNull(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_MOLGENIS_UI));
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_AUTHENTICATED), false);
		assertEquals(modelAndView.getModel().get("footerText"), "footerTest");
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_PLUGINID_WITH_QUERY_STRING),
				"plugin_id_test?entity=entityName");
	}

	@Test
	public void postHandleWithAppTrackingCode() throws Exception
	{
		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(AppTrackingCode.KEY_APP_TRACKING_CODE_FOOTER)).thenReturn(
				"alert('key_app_tracking_code_footer');");
		when(settings.getProperty(AppTrackingCode.KEY_APP_TRACKING_CODE_HEADER)).thenReturn(
				"alert('key_app_tracking_code_header');");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "app_tracking_code";

		HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);

		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(new MolgenisPluginController(uri)
		{
		});

		ModelAndView modelAndView = new ModelAndView();
		molgenisPluginInterceptor.postHandle(mockHttpServletRequest, null, handlerMethod, modelAndView);
		
		AppTrackingCodeImpl appTrackingCodeResult = new AppTrackingCodeImpl("alert('key_app_tracking_code_footer');",
				"alert('key_app_tracking_code_header');");
		assertEquals(modelAndView.getModel().get(MolgenisPluginInterceptor.APP_TRACKING_CODE_VARIABLE),
				appTrackingCodeResult);
	}

	@Test
	public void postHandle_authenticated() throws Exception
	{
		boolean isAuthenticated = true;
		when(authentication.isAuthenticated()).thenReturn(isAuthenticated);

		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "test";
		ModelAndView modelAndView = new ModelAndView();
		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(new MolgenisPluginController(uri)
		{
		});
		molgenisPluginInterceptor.postHandle(null, null, handlerMethod, modelAndView);
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_PLUGIN_ID), "test");
		assertNotNull(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_MOLGENIS_UI));
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_AUTHENTICATED), isAuthenticated);
		assertEquals(modelAndView.getModel().get("footerText"), "footerTest");
	}

	@Test
	public void postHandle_notAuthenticated() throws Exception
	{
		boolean isAuthenticated = false;
		when(authentication.isAuthenticated()).thenReturn(isAuthenticated);

		MolgenisSettings settings = mock(MolgenisSettings.class);
		when(settings.getProperty(MolgenisPluginInterceptor.KEY_FOOTER)).thenReturn("footerTest");
		MolgenisPluginInterceptor molgenisPluginInterceptor = new MolgenisPluginInterceptor(molgenisUi, settings);
		String uri = MolgenisPluginController.PLUGIN_URI_PREFIX + "test";
		ModelAndView modelAndView = new ModelAndView();
		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.getBean()).thenReturn(new MolgenisPluginController(uri)
		{
		});
		molgenisPluginInterceptor.postHandle(null, null, handlerMethod, modelAndView);
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_PLUGIN_ID), "test");
		assertNotNull(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_MOLGENIS_UI));
		assertEquals(modelAndView.getModel().get(MolgenisPluginAttributes.KEY_AUTHENTICATED), isAuthenticated);
		assertEquals(modelAndView.getModel().get("footerText"), "footerTest");
	}
}
