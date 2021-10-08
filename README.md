# ClinQ
Simple yet interesting validation mini-framework

## Features
- Declarative checks
```java
ClinQ.checker(Integer.class)
    .with(i -> i % 2 == 0)
    .with(i -> i > 100)
    .with(i -> i < 1000)
    .check(myInteger)
```

- Mapping on checks addition
```java
ClinQ.checker(Integer.class)
    .and(i -> i % 2 == 0)
    .map(Double::valueOf)  // Map integer to double and do further checks with it
    .and(d -> Math.pow(d, 3.5) > 500)
    .check(myInteger);
```

- Nested checks  
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
Then we can validate field-by-field using the nested checkers:
```java
var checker =
    ClinQ.checker(SampleDto.class)
        .with(Objects::nonNull)
        .with(SampleDto::getName, nameChecker ->
            nameChecker
                .with(Objects::nonNull)
                .with(str -> str.length() > 2))
        .with(SampleDto::getScores, scoresChecker ->
            scoresChecker
                .with(Objects::nonNull)
                .with(scores -> scores.size() > 2)
                .with(scores -> scores.stream().allMatch(score -> score > 0)))
        .with(dto -> dto.getName().length() == dto.getScores().size());

SampleDto one = new SampleDto("hi!", List.of(1, 2, 3));
Assertions.assertTrue(checker.check(one));
```

## Use it
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
        implementation 'com.github.theseems:ClinQ:v1.0-SNAPSHOT'
}
```

## Todo
- [ ] Debug tools (peek tokens, trace, etc.)
- [ ] Propagation strategy: should we fail? where?
- [ ] Dependent checks: make check depend on others.
