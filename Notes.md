#Proxy Deep Dive Notes

#Original Intro
Everybody knows the pattern proxy, but how can you use it effectively?
What kind of proxy patterns are available, and how can you
build patterns more effectively with it? Why is reflection needed for this?
Importantly, we need only the core JDK in most cases.

This tutorial starts from the basics and continues on to
DynamicProxies, DynamicObjectAdapter and DynamicStaticProxies at runtime,
ProxyComposite, and more.

How can we combine this with invokedynamic or
maybe with another language such as Kotlin?

The session, based on the German book Dynamic Proxies,
by Heinz Kabutz and the session’s presenter, takes a deep dive into this pattern group.



## Chapter 01
* Adapter v Delegator v Proxy
* Basic Proxy
  * SecurityProxy
  * RemoteProxy
  * VirtualProxy
    * ServiceFactory / ServiceStrategyFactory reduzieren auf eine generische Version
    * MethodScooped / einbauen fehlt noch bei jedem Method Invoke eine neue Instanz

* Combining Proxies - hand written / hard wired

## Chapter 02
* Dynamic Proxy
  * ProxyGenerator - FactoryMethod
  * ProxyBuilder - hand written / only same Interface
* Nested Builder
 * Reduktion
 * NestedBuilder Pattern - abstract and how to partly generate

Beispiel wie man Parameter für die Proxies per Nested Builder realisiert

## Chapter 03 - Performance - done
 * AnnotationProcessing
   * generated Delegator
   * generated VirtualProxy
   * generated VirtualProxy - ServiceFactory / ServiceStrategyFactory

## Chapter 04a - DynamicObjectAdapter - done
 * orig Version
 * typesafe Version
 * generated typesafe Version
 * use it like a Mock

## Chapter 04b - Static ObjectAdapter - done
 * handwritten typesafe Version
 * generated typesafe Version

 * ObjectAdapter mit einem VirtualProxy als Orig
 * use it like a Mock


 Slides ueberarbeiten

## Chapter 05 - ??

## Chapter 06 -Static Virtual Proxy at runtime - Source Generated - done
 * Heinz Version
 * OnExistingObject


* AnnotationProcessing for Nested - Builder - per Annotation @NestedBuilder
