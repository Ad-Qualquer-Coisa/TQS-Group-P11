package pt.ua.deti.tqs.p11;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/** Hexagonal architecture: dependencies point inwards (adapter, application, domain). */
@AnalyzeClasses(
    packages = "pt.ua.deti.tqs.p11",
    importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

  @ArchTest
  static final ArchRule domain_knows_no_framework_and_nothing_outside =
      noClasses()
          .that()
          .resideInAPackage("..p11.domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "org.springframework..", "jakarta..", "..p11.application..", "..p11.adapter..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule application_knows_no_adapter =
      noClasses()
          .that()
          .resideInAPackage("..p11.application..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..p11.adapter..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule feature_modules_form_no_cycles =
      slices()
          .matching("pt.ua.deti.tqs.p11.domain.(*)..")
          .should()
          .beFreeOfCycles()
          .allowEmptyShould(true);
}
