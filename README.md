# ClinQ

Simple yet interesting validation mini-framework

## Features

### Fluent checks

```java
ClinQ.<Integer>checker()
	.with(i -> i % 2 == 0)
	.with(i -> i > 100)
	.with(i -> i < 1000)
	.check(myInteger)
```

### Mappings

```java
ClinQ.<Integer>checker()
	.and(i -> i % 2 == 0)
	.map(Double::valueOf)  // Map integer to double and make further checks with it
	.and(d -> Math.pow(d, 3.5) > 500)
	.check(myInteger);
```

### Nested checks

For example we have a sample data transfer object:

```java
public class SampleDto {
	private final String name;
	private final List<Integer> scores;

	public String getName() {
		return name;
	}

	public List<Integer> getScores() {
		return scores;
	}

	public SampleDto(String name, List<Integer> scores) {
		this.name = name;
		this.scores = scores;
	}
}
```

Then we can validate it field-by-field using the nested checkers:

```java
var checker = ClinQ.<SampleDto>checker()
    .with(Objects::nonNull)
    .with(SampleDto::getName, nameChecker -> nameChecker
	    .with(Objects::nonNull)
	    .with(str -> str.length() > 2))
    .with(SampleDto::getScores, scoresChecker -> scoresChecker
	    .with(Objects::nonNull)
	    .with(scores -> scores.size() > 2)
	    .with(scores -> scores.stream().allMatch(score -> score > 0)))
    .with(dto -> dto.getName().length() == dto.getScores().size());

SampleDto one = new SampleDto("hi!", List.of(1, 2, 3));
Assertions.assertTrue(checker.check(one));
```

### Error description

#### In-place

You can specify error message together with check

```java
var checker = Clinq.<String>checker()
	.notNull("Input is null")
	.and("Length should be at least 2", name -> name.length() >= 2)
	.mapCheck("First char should be digit", name -> name.charAt(0), Character::isDigit)
	.mapCheck("Second char should be letter", name -> name.charAt(1), Character::isLetter);
```

#### Out-of-place

You can specify error message straight under the check

```java
var checker = Clinq.<String>checker()
	.notNull("Input is null")
	.and(name -> name.length() >= 2)
	    .error("Length should be at least 2")
	.mapCheck(name -> name.charAt(0), Character::isDigit)
	    .error("First char should be digit")
	.mapCheck(name -> name.charAt(1), Character::isLetter)
	    .error("Second char should be letter");
```

#### Collecting errors

You can collect errors with basically everything that can collect

```java
TestCheckErrors errors = new TestCheckErrors();
checker.check("i1nvalid", errors);
errors.assertSame(List.of("First char should be digit", "Second char should be letter"));
```

### Blocking checks

You can declare every check blocking. It means when it's failed the validation completely stops with the verdict:
incorrect.  
It can be declared in two styles similar to errors:

#### In-place

```java
var checker = Clinq.<String>checker()
	.notNull("Input is null")
	.and("Length should be at least 2", name -> name.length() >= 2)
	// notice that we won't go further if this fails
	.mapCheckBlocking("First char should be digit", name -> name.charAt(0), Character::isDigit)
	.mapCheck("Second char should be letter", name -> name.charAt(1), Character::isLetter);

TestCheckErrors errors = new TestCheckErrors();
checker.check("i1nvalid", errors);
errors.assertSame(List.of("First char should be digit"));
```

#### Out-of-place

```java
var checker = Clinq.<String>checker()
	.notNull("Input is null")
	.and(name -> name.length() >= 2)
	    .error("Length should be at least 2")
	.mapCheck(name -> name.charAt(0), Character::isDigit)
	    .error("First char should be digit")
	    .blocking()  // notice that we won't go further if this fails
	.mapCheck(name -> name.charAt(1), Character::isLetter)
	    .error("Second char should be letter");

TestCheckErrors errors = new TestCheckErrors();
checker.check("i1nvalid", errors);
errors.assertSame(List.of("First char should be digit"));
```

## How to use

Currently, this library can be fetched via JitPack:

[![](https://jitpack.io/v/theseems/ClinQ.svg)](https://jitpack.io/#theseems/ClinQ)

Here are specified instructions for Gradle (for maven etc. they can be found at JitPack - just click on the badge above)

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.theseems:ClinQ:v1.1'
}
```
