

/* First created by JCasGen Sun May 17 19:23:40 MSK 2015 */
package misis.chunkers.address.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun May 17 19:23:40 MSK 2015
 * XML source: /home/vladimir/nlp/MIsisAddress/desc/MisisAddressTypeSystem.xml
 * @generated */
public class AddressMention extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(AddressMention.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected AddressMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public AddressMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public AddressMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public AddressMention(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
}

    