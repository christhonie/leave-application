package za.co.dearx.leave;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("za.co.dearx.leave");

        noClasses()
            .that()
            .resideInAnyPackage("za.co.dearx.leave.service..")
            .or()
            .resideInAnyPackage("za.co.dearx.leave.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..za.co.dearx.leave.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
