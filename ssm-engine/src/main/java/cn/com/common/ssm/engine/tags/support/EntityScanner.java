package cn.com.common.ssm.engine.tags.support;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;

public class EntityScanner extends ClassPathBeanDefinitionScanner {

	private Class<? extends Annotation> annotationClass;

	private Class<?> markerInterface;

	public EntityScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

	public void registerFilters() {
		boolean acceptAllInterfaces = true;

		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		if (this.markerInterface != null) {
			addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
				@Override
				protected boolean matchClassName(String className) {
					return false;
				}
			});
			acceptAllInterfaces = false;
		}

		if (acceptAllInterfaces) {
			addIncludeFilter(new TypeFilter() {
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
					return true;
				}
			});
		}

		addExcludeFilter(new TypeFilter() {
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}

	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			for (BeanDefinition beanDefinition : candidates) {
				String beanName = beanDefinition.getBeanClassName();
				String simpleBeanName = beanName.substring(beanName.lastIndexOf(".") + 1);
				try {
					EntityHolder.registEntity(simpleBeanName, Class.forName(beanName));
				} catch (ClassNotFoundException e) {
					logger.error("", e);
				}
			}
		}
		return beanDefinitions;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (!beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
	}

	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
			logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface" + ". Bean already defined with the same name!");
			return false;
		}
	}

}
